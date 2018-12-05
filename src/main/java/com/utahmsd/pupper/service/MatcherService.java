package com.utahmsd.pupper.service;

import com.utahmsd.pupper.client.ZipCodeAPIClient;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dto.MatcherDataRequest;
import com.utahmsd.pupper.dto.MatcherDataResponse;
import com.utahmsd.pupper.dto.MatcherResponse;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.utahmsd.pupper.client.ZipCodeAPIClient.MAX_RADIUS;
import static com.utahmsd.pupper.dto.MatcherDataResponse.createMatcherDataResponse;
import static com.utahmsd.pupper.util.Constants.*;

@Named
@Singleton
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_ZIP_RADIUS = 3;
    private static final long DEFAULT_EXPIRES = 12L; //Mark matchResults to expire 12 hours from when a batch of profiles is sent


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
        List<MatchProfile> matchProfilesBatchWithRadiusFilter = getNextMatchProfileBatchWithZipFilter(matchProfileId, radius);
        if (matchProfilesBatchWithRadiusFilter == null || matchProfilesBatchWithRadiusFilter.isEmpty()) {
            return new ArrayList<>();
        }
        createBlankMatchResultRecordsForBatch(matchProfileId, matchProfilesBatchWithRadiusFilter);
        return ProfileCard.matchProfileToProfileCardMapper(matchProfilesBatchWithRadiusFilter);
    }

    /**
     * Retrieves a list of all matchProfiles that a given matchProfile has already been shown and has rated.
     * We want to filter these when retrieving the next batch of profile cards to display to the user, to prevent
     * profiles being displayed to the user multiple times.
     *
     * Expired match_results are intentionally excluded from this query, so they will be requeued and resent to the user.
     * @param matchProfileId
     * @return
     */
    private List<MatchProfile> getPastMatcherResultsForMatchProfile(Long matchProfileId) {
        Instant now = Instant.now();
        List<Long> previousMatchProfileIdsAndNotExpired =
                matchResultRepo.retrieveAllIdsforMatchProfilesSentInPreviousBatchesAndNotExpired(matchProfileId,
                                                                                                now,
                                                                                                matchProfileId, now);
        previousMatchProfileIdsAndNotExpired.remove(matchProfileId);

        return matchProfileRepo.findAllById(previousMatchProfileIdsAndNotExpired);

//        List<MatchProfile> activeProfiles = matchResultRepo.findActiveMatcherResults(matchProfileId);
//        List<MatchProfile> passiveProfiles = matchResultRepo.findPassiveMatcherResults(matchProfileId);
//        Set<MatchProfile> distinctProfiles = new HashSet<>();
//        distinctProfiles.addAll(activeProfiles);
//        distinctProfiles.addAll(passiveProfiles);

//        List<Long> ratedMatchProfileIds = matchResultRepo.retrieveAllIdsforMatchProfilesPreviouslyRated(matchProfileId, matchProfileId);

//        LOGGER.info("{}", distinctProfiles.size());
//        LOGGER.info("{}", ratedMatchProfileIds.size());
//        LOGGER.info("{}", previousMatchProfileIdsAndNotExpired.size());
//
//        return new ArrayList<>(distinctProfiles);
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
        List<Long> matchProfileIdsToExclude = new ArrayList<>();
        getPastMatcherResultsForMatchProfile(matchProfileId)
                .forEach(matchProfile -> matchProfileIdsToExclude.add(matchProfile.getId()));

        if (matchProfileIdsToExclude.isEmpty()) {
            List<MatchProfile> allRemainingProfiles = matchProfileRepo.findAll();
            allRemainingProfiles.removeIf(matchProfile -> matchProfile.getId().equals(matchProfileId));
            LOGGER.info("Total number of unseen matchProfiles: {}", allRemainingProfiles.size());

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

            List<Long> idList = getViewedMatchProfileIds(matchProfileId);

            if (idList.isEmpty()) {
                return matchProfileRepo.findAllByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId, zipcodesInRange);
            }
            return matchProfileRepo.
                    findAllByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(idList, matchProfileId, zipcodesInRange);
        }
        return null;
    }

    private List<MatchProfile> getNextMatchProfileBatchWithZipFilter(Long matchProfileId, int radius) {
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        int zipRadius = radius > 0 && radius < MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;
        if (m.isPresent()) {
            List<String> zipcodesInRange =
                    zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), String.valueOf(zipRadius));

            List<Long> viewedMatchProfileIds = getViewedMatchProfileIds(matchProfileId);

            if (viewedMatchProfileIds.isEmpty()) {
                return matchProfileRepo.findTop5ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId,
                        zipcodesInRange);
            }
            return matchProfileRepo.
                    findTop5ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(viewedMatchProfileIds,
                            matchProfileId, zipcodesInRange);
        }
        return null;
    }

    //    @Transactional
    private void createBlankMatchResultRecordsForBatch(Long matchProfileId, List<MatchProfile> matchProfiles) {
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
        updateMatchResultRecord(matchProfileOneId, matchProfileTwoId, isMatch, result);
    }

    private void updateMatchResultRecord(Long matchProfileId, Long resultForMatchProfileId, Boolean isMatch, MatchResult matchResult) {
        Instant resultCompleted = Instant.now();
        if (matchResult.isMatchForProfileTwo() == null && matchResult.isMatchForProfileOne() == null) {
            //Neither result for the match result record is filled in, hold off on marking the record as completed

            resultCompleted = null;
            LOGGER.info("Existing but empty match result record found, updating matcher response but still waiting for " +
                    "other user to respond.");
        }

        if (matchResult.getMatchProfileOne().getId().equals(matchProfileId)) { //MatchProfileOne is making update to matchResult
            matchResultRepo.updateToMatchResultByMatchProfileOne(isMatch, resultCompleted, matchProfileId, resultForMatchProfileId);
        } else {
            //Then matchProfileId corresponds to matchProfileTwo in match_result record
            matchResultRepo.updateToMatchResultByMatchProfileTwo(isMatch, resultCompleted, resultForMatchProfileId, matchProfileId);
        }
    }

    private List<Long> getViewedMatchProfileIds(Long matchProfileId) {
        List<Long> idList = new ArrayList<>();
        getPastMatcherResultsForMatchProfile(matchProfileId)
                .forEach(each -> idList.add(each.getId()));
//        LOGGER.info("Number of ids corresponding to matchProfiles to exclude from future matcher batches: {}", idList.size());
        return idList;
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
        return createMatcherDataResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public List<MatchResult> retrieveMatchResultDataForMatchProfile(Long matchProfileId) {
        List<MatchResult> completedMatchResults = matchResultRepo.findCompletedMatchResultsForMatchProfile(matchProfileId);
        LOGGER.info("Found {} completed matchResults corresponding to matchProfileId={}", completedMatchResults.size(), matchProfileId);

        return completedMatchResults;
    }

    public void deleteMatchResultForMatchProfiles(Long matchProfileId1, Long matchProfileId2) {
        matchResultRepo.deleteMatchResultByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    public void deleteAllPupperMatchResults(Long matchProfileId) { } //Delete all match result data corresponding to a match profile

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
        } else {
            LOGGER.info("Valid match result.");
        }
        return result;
    }

}
