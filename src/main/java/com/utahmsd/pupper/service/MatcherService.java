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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.utahmsd.pupper.client.ZipCodeAPIClient.MAX_RADIUS;
import static com.utahmsd.pupper.dto.MatcherDataResponse.createMatcherDataResponse;
import static com.utahmsd.pupper.dto.pupper.ProfileCard.matchProfileToProfileCardMapper;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.IDS_MISMATCH;
import static com.utahmsd.pupper.util.Utils.getIsoFormatTimestamp;
import static org.springframework.http.HttpStatus.OK;

@Service
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_ZIP_RADIUS = 3;
    private static final long DEFAULT_EXPIRES = 24*7L; //Mark matchResult record expired 7 days from when the record is created
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
        if (profileCards.isEmpty()) {
            return;
        }
        String zipCode = m.getUserProfile().getZip();
        List<String> zipcodeList = new ArrayList<>();
        profileCards.forEach(each -> {
            if (!zipcodeList.contains(each.getDistance())) {
                zipcodeList.add(each.getDistance());
            }
        });
        Map<String, Integer> zipcodeDistances =
                zipCodeAPIClient.getDistanceBetweenZipCodeAndMultipleZipCodes(zipCode, zipcodeList);

        profileCards.forEach(profileCard -> {
            Integer numMilesAway = zipcodeDistances.get(profileCard.getDistance());

            profileCard.setDistance(String.format(DIST_AWAY, numMilesAway));
        });
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
    private List<Long> getIdsOfPreviouslyShownProfilesForMatchProfile(Long matchProfileId) {

        //Native query referencing matchProfileIds returns a list of Integers **
        List<Integer> previouslyRatedProfileIds =
                matchResultRepo.retrieveAllIdsforMatchProfilesPreviouslyRated(matchProfileId, matchProfileId);

        LOGGER.info("Number of profiles that this matchprofile has rated: {}", previouslyRatedProfileIds.size());

        Set<Long> distinctMatchProfileIds = new HashSet<>();
        //Cast each Integer to a Long to prevent hashset from including duplicate values
        previouslyRatedProfileIds.forEach(each -> distinctMatchProfileIds.add(each.longValue()));

        //JPQL query referencing matchProfileIds returns a list of Longs **
        List<Long> previouslySentRecords =
                matchResultRepo.findMatchProfilesInPreviouslySentBatchesNotExpired(matchProfileId, Instant.now());
        LOGGER.info("Number of records in batches that were previously sent to the user (that are not expired): {}", previouslySentRecords.size());

        distinctMatchProfileIds.addAll(previouslySentRecords);

        LOGGER.info("Number of distinct ids in the combined queries: {}", distinctMatchProfileIds.size());
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
        List<Long> matchProfileIdsToExclude = getIdsOfPreviouslyShownProfilesForMatchProfile(matchProfileId);

        List<MatchProfile> unseenProfiles =
                matchProfileRepo.findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(matchProfileIdsToExclude, matchProfileId);

        LOGGER.info("Total number of unseen matchProfiles: {}", unseenProfiles.size());
        return unseenProfiles;
    }

    public List<MatchProfile> getAllUnseenMatchProfilesForMatchProfileWithZipFilter(Long matchProfileId, int radius) {
        int zipRadius = radius > 0 && radius <= MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        if (m.isPresent()) {
            List<String> zipcodesInRange = zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), String.valueOf(zipRadius));

            List<Long> idList = getIdsOfPreviouslyShownProfilesForMatchProfile(matchProfileId);

            List<MatchProfile> unseenProfiles = matchProfileRepo.
                    findAllByIdIsNotInAndUserProfile_ZipIsInOrderByScoreDesc(idList, zipcodesInRange);

            unseenProfiles.remove(m.get());
            return unseenProfiles;
        }
        return null;
    }

    private List<MatchProfile> getNextMatchProfileBatchWithZipFilter(MatchProfile matchProfile, int radius) {
        int zipRadius = radius > 0 && radius <= MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;

        List<String> zipcodesInRange =
                zipCodeAPIClient.getZipCodesInRadius(matchProfile.getUserProfile().getZip(), String.valueOf(zipRadius));
        if (zipcodesInRange.isEmpty()) {
            LOGGER.error("No zipcodes were found within the specified radius of the profile's zipcode.");
            return new ArrayList<>();
        }
        List<Long> viewedMatchProfileIds = getIdsOfPreviouslyShownProfilesForMatchProfile(matchProfile.getId());

        List<MatchProfile> nextBatch =
                matchProfileRepo.findTop3ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(viewedMatchProfileIds,
                        matchProfile.getId(), zipcodesInRange);

        LOGGER.info("Number of profiles in the next batch: {}", nextBatch.size());
        return nextBatch;
    }

    void createBlankMatchResultRecordsForBatch(Long matchProfileId, List<MatchProfile> matchProfiles) {
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
        LOGGER.info("Request contains {} match_result records to update.", request.getMap().size());
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

    public List<MatchResult> retrieveCompletedMatchResultsForMatchProfile(Long matchProfileId) {
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

    public Integer retrieveNumberOfExpiredIncompleteRecords() {
        return matchResultRepo.getExpiredIncompleteRecordsCount(Instant.now());
    }

    public void extendResultExpirationPeriodForIncompleteRecords() {
        Instant newExpiresAt = Instant.now().plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
        LOGGER.info("Extending expiration for expired/incomplete records to '{}'", getIsoFormatTimestamp(newExpiresAt));
        matchResultRepo.extendExpirationForIncompleteRecords(newExpiresAt);
    }

}
