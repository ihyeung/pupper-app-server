package com.utahmsd.pupper.service;

import com.utahmsd.pupper.client.ZipCodeAPIClient;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dto.MatcherDataRequest;
import com.utahmsd.pupper.dto.MatcherDataResponse;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.utahmsd.pupper.client.ZipCodeAPIClient.MAX_RADIUS;
import static com.utahmsd.pupper.dto.MatcherDataResponse.createMatcherDataResponse;
import static com.utahmsd.pupper.dto.pupper.ProfileCard.matchProfileToProfileCardMapper;
import static com.utahmsd.pupper.util.Constants.*;
import static org.springframework.http.HttpStatus.OK;

@Named
@Singleton
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_ZIP_RADIUS = 3;
    private static final long DEFAULT_EXPIRES = 12L; //Mark matchResults to expire 12 hours from when a batch of profiles is sent
    private final String DIST_AWAY = "%d miles away";


    private final MatchProfileRepo matchProfileRepo;
    private final MatchResultRepo matchResultRepo;
    private final ZipCodeAPIClient zipCodeAPIClient;

    @Autowired
    public MatcherService(final MatchProfileRepo matchProfileRepo,
                          final MatchResultRepo matchResultRepo,
                          final ZipCodeAPIClient zipCodeAPIClient) {
        this.matchProfileRepo = matchProfileRepo;
        this.matchResultRepo = matchResultRepo;
        this.zipCodeAPIClient = zipCodeAPIClient;
    }

    public List<ProfileCard> retrieveMatcherDataProfileCards(Long matchProfileId, int radius) {
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        if (m.isPresent()) {
            List<MatchProfile> matchProfilesBatchWithRadiusFilter = getNextMatchProfileBatchWithZipFilter(m.get(), radius);
            if (matchProfilesBatchWithRadiusFilter.isEmpty()) {
                return new ArrayList<>();
            }
            createBlankMatchResultRecordsForBatch(matchProfileId, matchProfilesBatchWithRadiusFilter);
            List<ProfileCard> profileCards = matchProfileToProfileCardMapper(matchProfilesBatchWithRadiusFilter);
            updateProfileCardDistancesFromZipcode(m.get(), profileCards);
            return profileCards;
        }
        return new ArrayList<>();
    }

    private void updateProfileCardDistancesFromZipcode(MatchProfile m, List<ProfileCard> profileCards) {
        List<String> zipcodeList = new ArrayList<>();
        profileCards.forEach(each -> zipcodeList.add(each.getDistance()));
        Map<String, Integer> zipcodeDistances =
                zipCodeAPIClient.getDistanceBetweenMultipleZipcodes(m.getUserProfile().getZip(), zipcodeList);

        profileCards.forEach(profileCard ->
                profileCard.setDistance(String.format(DIST_AWAY, zipcodeDistances.get(profileCard.getDistance()))));
    }

    /**
     * Retrieves a list of all matchProfiles that either:
     * 1. A given matchProfile has already been previously shown and has rated (and the result was saved to the db).
     * 2. A previously sent batch of matcher data (profile cards) was sent to a given matchProfile. In this situation
     * we want to exclude these profiles IF the record is not expired yet. (if it is expired and not marked complete,
     * we want the record to be requeued and resent to the user -- i.e., if the batch was never received by the client)
     *
     * We want to filter these when retrieving the next batch of profile cards to display to the user, to prevent
     * profiles being displayed to the user multiple times.
     *
     * Expired match_results are intentionally excluded from this query, so they will be requeued and resent to the user.
     * @param matchProfileId
     * @return
     */
    private List<Long> getPastMatcherResultsForMatchProfile(Long matchProfileId) {

        //Native query referencing matchProfileIds returns a list of Integers **
        List<Integer> previouslyRatedProfileIdsUsingNativeQuery =
                matchResultRepo.retrieveAllIdsforMatchProfilesPreviouslyRated(matchProfileId, matchProfileId);

        Set<Long> distinctMatchProfileIds = new HashSet<>();
        //Cast each Integer to a Long to prevent hashset from including duplicate values
        previouslyRatedProfileIdsUsingNativeQuery.forEach(each -> distinctMatchProfileIds.add(each.longValue()));

        //JPQL query referencing matchProfileIds returns a list of Longs **
        List<Long> previouslySentRecords =
                matchResultRepo.findMatchProfilesInPreviouslySentBatchesNotExpired(matchProfileId, Instant.now());

        distinctMatchProfileIds.addAll(previouslySentRecords);

        LOGGER.info("Number of distinct ids in combined two queries: {}", distinctMatchProfileIds.size());
        return new ArrayList<>(distinctMatchProfileIds);
    }

    /**
     * Retrieves a list of matchProfiles corresponding to profileCards that the user has not yet
     * been shown and/or rated.
     *
     * The matchProfiles returned in the batch are those with the highest scores that the user has not viewed/rated.
     * @param matchProfileId
     * @return
     */
    public List<MatchProfile> getAllUnseenMatchProfilesForMatchProfile(Long matchProfileId) {
        List<Long> matchProfileIdsToExclude = getPastMatcherResultsForMatchProfile(matchProfileId);

        if (matchProfileIdsToExclude.isEmpty()) {
            List<MatchProfile> allRemainingProfiles = matchProfileRepo.findAll();
            allRemainingProfiles.removeIf(matchProfile -> matchProfile.getId().equals(matchProfileId));

            return allRemainingProfiles;
        }
        List<MatchProfile> unseenProfiles =
                matchProfileRepo.findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(matchProfileIdsToExclude, matchProfileId);
        LOGGER.info("Total number of unseen matchProfiles: {}", unseenProfiles.size());
        return unseenProfiles;
    }

    public List<MatchProfile> getAllUnseenMatchProfilesForMatchProfileWithZipFilter(Long matchProfileId, int radius) {
        int zipRadius = radius > 0 && radius < MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        if (m.isPresent()) {
            List<String> zipcodesInRange = zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), String.valueOf(zipRadius));

            List<Long> idList = getPastMatcherResultsForMatchProfile(matchProfileId);

            if (idList.isEmpty()) {
                return matchProfileRepo.findAllByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId, zipcodesInRange);
            }
            return matchProfileRepo.
                    findAllByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(idList, matchProfileId, zipcodesInRange);
        }
        return null;
    }

    private List<MatchProfile> getNextMatchProfileBatchWithZipFilter(MatchProfile matchProfile, int radius) {
        int zipRadius = radius > 0 && radius <= MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;

        List<String> zipcodesInRange =
                zipCodeAPIClient.getZipCodesInRadius(matchProfile.getUserProfile().getZip(), String.valueOf(zipRadius));

        List<Long> viewedMatchProfileIds = getPastMatcherResultsForMatchProfile(matchProfile.getId());

        if (viewedMatchProfileIds.isEmpty()) {
            return matchProfileRepo.findTop5ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfile.getId(),
                    zipcodesInRange);
        }
        List<MatchProfile> nextBatch = matchProfileRepo.
                findTop5ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(viewedMatchProfileIds,
                        matchProfile.getId(), zipcodesInRange);
        return nextBatch;
    }

//    @Transactional
    void createBlankMatchResultRecordsForBatch(Long matchProfileId, List<MatchProfile> matchProfiles) {
        LOGGER.info("Creating empty match_result records for outgoing batch of matcher data");
        matchProfiles.forEach(each -> insertOrUpdateMatchResult(matchProfileId, each.getId(), null));
    }

    public void insertOrUpdateMatchResult(Long matchProfileOneId, Long matchProfileTwoId, Boolean isMatch) {
        MatchResult result = matchResultRepo.findMatcherRecord(matchProfileOneId, matchProfileTwoId);
        if (result == null) {
            Instant batchSent = Instant.now();
            Instant recordExpires = batchSent.plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
            matchResultRepo.insertMatchResult(matchProfileOneId, matchProfileTwoId, isMatch, batchSent, recordExpires);
            return;
        }
        if (isMatch != null) {
            updateMatchResultRecord(matchProfileOneId, matchProfileTwoId, isMatch, result);
        }
    }

    private void updateMatchResultRecord(Long matchProfileId, Long resultForMatchProfileId, Boolean isMatch, MatchResult matchResult) {
        Instant resultCompleted = Instant.now();
        if (matchResult.getMatchProfileOne().getId().equals(matchProfileId)) {
            matchResultRepo.updateMatchResultByMatchProfileOne(isMatch, matchProfileId, resultForMatchProfileId);
        }
        else {
            matchResultRepo.updateMatchResultByMatchProfileTwo(isMatch, resultForMatchProfileId, matchProfileId);
        }
        if (matchResult.isMatchForProfileOne() != null && matchResult.isMatchForProfileTwo() != null) {
            LOGGER.info("Both match profiles have rated each other, marking matchResult record as completed");
            matchResultRepo.markMatchResultAsCompleted(matchResult.getId(), resultCompleted);
        }
    }

    public MatcherDataResponse updateMatcherResults (Long matchProfileId, MatcherDataRequest request) {
        LOGGER.info("{} match_result records to update data for", request.getMap().size());
        AtomicInteger updateCount = new AtomicInteger();
        if (!request.getMatchProfileId().equals(matchProfileId)) {
            return createMatcherDataResponse(false, null, HttpStatus.BAD_REQUEST, IDS_MISMATCH);
        }
        request.getMap().forEach((id,isMatch) -> {
            MatchResult matchResultRecord = matchResultRepo.findMatcherRecord(matchProfileId, id);
            if (matchResultRecord == null) {
                LOGGER.error("Trying to update a matchResult record in batch but record does not exist.");
            } else {
                updateMatchResultRecord(matchProfileId, id, isMatch, matchResultRecord);
                updateCount.getAndIncrement();
            }
        });
        LOGGER.info("{} records were successfully updated", updateCount);
        return createMatcherDataResponse(true, null, OK, DEFAULT_DESCRIPTION);
    }

    public List<MatchResult> retrieveMatchResultDataForMatchProfile(Long matchProfileId) {
        List<MatchResult> completedMatchResults =
                                        matchResultRepo.findCompletedMatchResultsForMatchProfile(matchProfileId);
        LOGGER.info("Found {} completed matchResults corresponding to matchProfileId={}",
                completedMatchResults.size(), matchProfileId);

        return completedMatchResults;
    }

    public void unmatchWithMatchProfile(Long matchProfileId, Long matchProfileToUnmatchWith) {
        MatchResult result = checkForTwoWayMatch(matchProfileId, matchProfileToUnmatchWith);
        if (result != null) {

            Instant unmatchTimestamp = Instant.now();
            if (result.getMatchProfileOne().getId().equals(matchProfileId)) {
                matchResultRepo.updateMatchResultByMatchProfileOne(false,
                        matchProfileId, matchProfileToUnmatchWith);
            } else if (result.getMatchProfileTwo().getId().equals(matchProfileId)) {
                matchResultRepo.updateMatchResultByMatchProfileTwo(false,
                        matchProfileToUnmatchWith, matchProfileId);
            }
            matchResultRepo.markMatchResultAsCompleted(result.getId(), unmatchTimestamp);
        }
    }

    public void deleteMatchResultRecordForMatchProfiles(Long matchProfileId1, Long matchProfileId2) {
        matchResultRepo.deleteMatchResultByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    public void deleteAllMatchResultDataByMatchProfileId(Long matchProfileId) {
        matchResultRepo.deleteMatchResultByMatchProfileOne_IdOrMatchProfileTwo_Id(matchProfileId, matchProfileId);
    }

    /**
     * Scheduled task to clean up expired match_result records, that kicks off every night at midnight.
     */
//    @Scheduled(cron = "0 0 0 * * *") //
    private void deleteIncompleteExpiredMatchResults() {
        matchResultRepo.deleteIncompleteExpiredMatcherRecords(Instant.now());
    }

    public MatchResult checkForTwoWayMatch(Long matchProfileId, Long matchProfileId2) {
        MatchResult result = matchResultRepo.findMatcherRecord(matchProfileId, matchProfileId2);
        if (result == null || !result.isMatchForProfileOne() || !result.isMatchForProfileTwo()) {
            LOGGER.info("Invalid match result.");
            return null;
        }
        return result;
    }

    public void updateRecordExpiresForAllFields() {
        Instant batchSent = Instant.now();
        Instant recordExpires = batchSent.plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
        matchResultRepo.updateRecordExpiresTimeTest(recordExpires);
    }

}
