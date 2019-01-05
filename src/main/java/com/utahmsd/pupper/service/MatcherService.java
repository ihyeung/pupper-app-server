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

    public List<ProfileCard> retrieveMatcherDataProfileCards(Long matchProfileId, boolean randomize, boolean calculateDistances) {
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);

        if (m.isPresent()) {
            List<MatchProfile> matchProfilesBatchWithRadiusFilter = getNextMatchProfileBatch(m.get(), randomize);
            if (matchProfilesBatchWithRadiusFilter.isEmpty()) {
                return Collections.emptyList();
            }
            Map<Long, Boolean> otherUserOutcomesForMatchProfile =
                    createBlankMatchResultRecordsForBatch(matchProfileId, matchProfilesBatchWithRadiusFilter);

            List<ProfileCard> profileCards = matchProfileToProfileCardMapper(matchProfilesBatchWithRadiusFilter);

            if (calculateDistances) { //Convert zip codes on profile cards to distances using additional zip code api calls
                setProfileCardDistancesUsingZipCodes(m.get(), profileCards);
            }

            profileCards.forEach(card -> card.setMatch(otherUserOutcomesForMatchProfile.get(card.getProfileId())));

            return profileCards;
        }
        return Collections.emptyList();
    }

    private void setProfileCardDistancesUsingZipCodes(MatchProfile m, List<ProfileCard> profileCards) {
        if (profileCards.isEmpty()) {
            return;
        }
        String zipCode = m.getUserProfile().getZip();
        Set<String> zipCodeList = new HashSet<>();
        profileCards.forEach(card -> zipCodeList.add(card.getDistance()));
        Map<String, Integer> zipcodeDistances =
                zipCodeAPIClient.getDistanceBetweenZipCodeAndMultipleZipCodes(zipCode, new ArrayList<>(zipCodeList));

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

        // LOGGER.info("Number of profiles that this matchprofile has rated: {}", previouslyRatedProfileIds.size());
        Set<Long> distinctMatchProfileIds = new HashSet<>();
        //Cast each Integer to a Long to prevent hashset from including duplicate values
        previouslyRatedProfileIds.forEach(each -> distinctMatchProfileIds.add(each.longValue()));

        //JPQL query referencing matchProfileIds returns a list of Longs **
        List<Long> previouslySentRecords =
                matchResultRepo.findMatchProfilesInPreviouslySentBatchesNotExpired(matchProfileId, Instant.now());
        // LOGGER.info("Number of records in batches that were previously sent to the user (that are not expired): {}", previouslySentRecords.size());

        distinctMatchProfileIds.addAll(previouslySentRecords);

        // LOGGER.info("Number of distinct ids in the combined queries: {}", distinctMatchProfileIds.size());
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
        return matchProfileRepo.findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(matchProfileIdsToExclude, matchProfileId);
    }

    private List<MatchProfile> getNextMatchProfileBatch(MatchProfile matchProfile, boolean randomize) {
        List<Long> viewedMatchProfileIds = getIdsOfPreviouslyShownProfilesForMatchProfile(matchProfile.getId());
        if (randomize) { //Don't make zipcode api call, just retrieve match profiles randomly
            return matchProfileRepo.findTop3ByIdIsNotInAndIdIsNotOrderByScoreDesc(viewedMatchProfileIds, matchProfile.getId());
        } else {
            return getNextMatchProfileBatchWithZipFilter(matchProfile, viewedMatchProfileIds);
        }
    }

    private List<MatchProfile> getNextMatchProfileBatchWithZipFilter(MatchProfile matchProfile, List<Long> viewedProfileIds) {

        LOGGER.info("Retrieving match profiles with zip filter: matchProfileId={} has a zip code radius of {} miles", matchProfile.getId(), matchProfile.getZipRadius());
        int zipRadius =
                matchProfile.getZipRadius() > 0 && matchProfile.getZipRadius() <= MAX_RADIUS ? matchProfile.getZipRadius() : DEFAULT_ZIP_RADIUS;

        List<String> zipcodesInRange =
                zipCodeAPIClient.getZipCodesInRadius(matchProfile.getUserProfile().getZip(), String.valueOf(zipRadius));
        if (zipcodesInRange.isEmpty()) {
            LOGGER.error("No zipcodes were found within the specified radius of the profile's zipcode.");
            return Collections.emptyList();
        }
        return matchProfileRepo.findTop3ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(
                        viewedProfileIds,
                        matchProfile.getId(),
                        zipcodesInRange);
    }

    /**
     *
     * @param matchProfileId The active user requesting or updating matching data.
     * @param matchProfiles The list of profiles that the active user has requested or is making updates for.
     * @return Returns a Map<Long,Boolean> containing match result data for match profiles in matchProfiles that have
     * separately already liked/disliked the profile of matchProfileId. Each key-value pair in the map corresponds to
     * the a match prfoile id and their response regarding the active user.
     *
     * If the other user has not yet rated the active user, the corresponding value in the map is null.
     */
    Map<Long, Boolean> createBlankMatchResultRecordsForBatch(Long matchProfileId, List<MatchProfile> matchProfiles) {
        Map<Long, Boolean> wasMatchForOtherUsers = new HashMap<>(); //Map storing other users' responses to the active user

        matchProfiles.forEach(each -> {
            Optional<MatchResult> result = matchResultRepo.getMatchResult(matchProfileId, each.getId());
            if (!result.isPresent()) {
                wasMatchForOtherUsers.put(each.getId(), null);

            } else {
                result.ifPresent(r -> {
                    if (r.getMatchProfileOne().getId().equals(matchProfileId)) {
                        wasMatchForOtherUsers.put(each.getId(), r.isMatchForProfileTwo());
                    } else {
                        wasMatchForOtherUsers.put(each.getId(), r.isMatchForProfileOne());
                    }
                });
            }
            //Use map-stored value for each profile id key to help with logic determining whether to insert/update
            insertOrUpdateMatchResult(matchProfileId, each.getId(), wasMatchForOtherUsers.get(each.getId()));
        });

        return wasMatchForOtherUsers;
    }

    public void insertOrUpdateMatchResult(Long matchProfileOneId, Long matchProfileTwoId, Boolean isMatch) {
        if (isMatch == null) { //Definitively know no match result exists, perform insert
            insertMatchResult(matchProfileOneId, matchProfileTwoId, null);
            return;
        }
        Optional<MatchResult> result = matchResultRepo.getMatchResult(matchProfileOneId, matchProfileTwoId);
        if (result.isPresent()) {
            updateMatchResultRecord(matchProfileOneId, matchProfileTwoId, isMatch, result.get());
        } else {
            insertMatchResult(matchProfileOneId, matchProfileTwoId, isMatch);
        }
    }

    private void insertMatchResult(Long matchProfileOneId, Long matchProfileTwoId, Boolean isMatch) {
        Instant batchSent = Instant.now();
        Instant recordExpires = batchSent.plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
        matchResultRepo.insertMatchResult(matchProfileOneId, matchProfileTwoId, isMatch, batchSent, recordExpires);
    }

    private void updateMatchResultRecord(Long matchProfileId, Long resultForMatchProfileId, Boolean isMatch, MatchResult matchResult) {
        if (matchResult.getMatchProfileOne().getId().equals(matchProfileId)) {
            matchResultRepo.updateMatchResultByMatchProfileOne(isMatch, matchProfileId, resultForMatchProfileId);
        } else {
            matchResultRepo.updateMatchResultByMatchProfileTwo(isMatch, resultForMatchProfileId, matchProfileId);
        }
        matchResultRepo.updateMatchResultRecordIfCompleted(matchResult.getId(), Instant.now());
    }

    public MatcherDataResponse updateMatcherResults (Long matchProfileId, MatcherDataRequest request) {
//        LOGGER.info("Request contains {} match_result records to update.", request.getMap().size());
        AtomicInteger updateCount = new AtomicInteger();
        if (!request.getMatchProfileId().equals(matchProfileId)) {
            return createMatcherDataResponse(false, null, HttpStatus.BAD_REQUEST, IDS_MISMATCH);
        }
        request.getMap().forEach((id,isMatch) -> {
            Optional<MatchResult> matchResultRecord = matchResultRepo.getMatchResult(matchProfileId, id);
            if (matchResultRecord.isPresent()) {
                updateMatchResultRecord(matchProfileId, id, isMatch, matchResultRecord.get());
                updateCount.getAndIncrement();
            } else {
                LOGGER.error("Trying to update a matchResult record in batch but record does not exist.");
            }
        });
        LOGGER.info("{} records were successfully updated", updateCount);
        return createMatcherDataResponse(true, null, OK, DEFAULT_DESCRIPTION);
    }

    public List<MatchResult> retrieveCompletedMatchResultsForMatchProfile(Long matchProfileId) {
        List<MatchResult> completedMatchResults =
                matchResultRepo.findCompletedMatchResultsForMatchProfile(matchProfileId);
//        LOGGER.info("Found {} completed matchResults corresponding to matchProfileId={}",
//                completedMatchResults.size(), matchProfileId);

        return completedMatchResults;
    }

    public void unmatchWithMatchProfile(Long matchProfileId, Long matchProfileToUnmatchWith) {
        MatchResult result = checkForTwoWayMatch(matchProfileId, matchProfileToUnmatchWith);
        if (result != null) {
            Instant unmatchTimestamp = Instant.now();
            if (result.getMatchProfileOne().getId().equals(matchProfileId)) {
                matchResultRepo.updateMatchResultByMatchProfileOne(false, matchProfileId,
                        matchProfileToUnmatchWith);
            } else {
                matchResultRepo.updateMatchResultByMatchProfileTwo(false, matchProfileToUnmatchWith,
                        matchProfileId);
            }
            matchResultRepo.updateMatchResultRecordIfCompleted(result.getId(), unmatchTimestamp);
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
        Optional<MatchResult> result = matchResultRepo.getMatchResult(matchProfileId, matchProfileId2);
        if (!result.isPresent()) {
            LOGGER.error("Invalid match result.");
            return null;
        }
        else if (!result.get().isMatchForProfileOne() || !result.get().isMatchForProfileTwo()) {
            LOGGER.error("Not a mutual match.");
            return null;
        }
        return result.get();
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
