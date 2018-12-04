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

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.utahmsd.pupper.client.ZipCodeAPIClient.MAX_RADIUS;
import static com.utahmsd.pupper.dto.MatcherDataResponse.createMatcherDataResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.NOT_FOUND;

@Named
@Singleton
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_BATCH_SIZE = 3; //Number of match profiles to retrieve for a user per batch
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
        List<MatchProfile> matchProfiles = getNextMatchProfileBatchWithZipFilter(matchProfileId, radius);

        createMatchRecordsForBatch(matchProfileId, matchProfiles);
        return ProfileCard.matchProfileToProfileCardMapper(matchProfiles);
    }

    /**
     * Retrieves a list of all matchProfiles that a given matchProfile has already been shown and has rated.
     * We want to filter these when retrieving the next batch of profile cards to display to the user, to prevent
     * profiles being displayed to the user multiple times.
     * @param matchProfileId
     * @return
     */
    private List<MatchProfile> getPastMatcherResultsForMatchProfile(Long matchProfileId) {
        List<MatchProfile> activeProfiles = matchResultRepo.findActiveMatcherResults(matchProfileId);
        List<MatchProfile> passiveProfiles = matchResultRepo.findPassiveMatcherResults(matchProfileId);
        Set<MatchProfile> distinctProfiles = new HashSet<>();
        distinctProfiles.addAll(activeProfiles);
        distinctProfiles.addAll(passiveProfiles);

        return new ArrayList<>(distinctProfiles);
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
        List<Long> idList = new ArrayList<>();
        getPastMatcherResultsForMatchProfile(matchProfileId)
                .forEach(matchProfile -> idList.add(matchProfile.getId()));

        List<MatchProfile> unseenProfiles = matchProfileRepo.findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(idList, matchProfileId);
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

    public List<MatchProfile> getNextMatchProfileBatchWithZipFilter(Long matchProfileId, int radius) {
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        int zipRadius = radius > 0 && radius < MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;
        if (m.isPresent()) {
            List<String> zipcodesInRange = zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), String.valueOf(zipRadius));
            LOGGER.info("Number of zipcodes in range of {}: {}", m.get().getUserProfile().getZip(), zipcodesInRange.size());

            List<Long> idList = getViewedMatchProfileIds(matchProfileId);

            if (idList.isEmpty()) {
                return matchProfileRepo.findTop5ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId,
                                                                                                zipcodesInRange);
            }
            return matchProfileRepo.
                        findTop5ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(idList,
                                                                                            matchProfileId, zipcodesInRange);
        }
        return null;
    }

//    @Transactional
    public void createMatchRecordsForBatch(Long matchProfileId, List<MatchProfile> matchProfiles) {
        Instant batchSent = Instant.now();
        Instant recordExpires = batchSent.plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
        matchProfiles.forEach(matchProfile ->
                matchResultRepo.insertMatchResult(matchProfileId, matchProfile.getId(), null, batchSent, recordExpires));
    }

    public void createOrInsertMatchResultRecord(Long matchProfileOneId, Long matchProfileTwoId, Boolean isMatch) {
        MatchResult result = matchResultRepo.findMatchResult(matchProfileOneId, matchProfileTwoId);
        if (result == null) {
            Instant batchSent = Instant.now();
            Instant recordExpires = batchSent.plus(DEFAULT_EXPIRES, ChronoUnit.HOURS);
            matchResultRepo.insertMatchResult(matchProfileOneId, matchProfileTwoId, isMatch, batchSent, recordExpires);
            LOGGER.info("New match result record has been created");

            return;
        }
        updateMatchResultRecord(matchProfileOneId, matchProfileTwoId, isMatch);
    }

    private void updateMatchResultRecord(Long matchProfileId1, Long matchProfileId2, Boolean isMatch) {
        Instant resultCompleted = Instant.now();
        matchResultRepo.updateMatchResult(isMatch, resultCompleted, matchProfileId2, matchProfileId1); //Swap the order of matchProfileIds since a record already exists
        LOGGER.info("Existing match result record has been updated and marked as completed.");
    }

    private List<Long> getViewedMatchProfileIds(Long matchProfileId) {
        List<Long> idList = new ArrayList<>();
        getPastMatcherResultsForMatchProfile(matchProfileId)
                .forEach(each -> idList.add(each.getId()));
        LOGGER.info("Number of ids to exclude: {}", idList.size());
        return idList;
    }

    public MatcherDataResponse updateMatcherResults (Long matchProfileId, MatcherDataRequest request) {
        if (request.getMatchProfileId().equals(matchProfileId)) {
            return createMatcherDataResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        request.getMap().forEach((id,result) -> updateMatchResultRecord(matchProfileId, id, result));
        return createMatcherDataResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatcherResponse deleteAllPupperMatchResults(Long matchProfileId) { return null;} //Delete all match result data corresponding to a match profile

    public MatchResult checkForMatch(Long matchProfileId, Long matchProfileId2) {
        MatchResult result = matchResultRepo.findMatchResult(matchProfileId, matchProfileId2);
        if (result == null || !result.isMatchForProfileOne() || !result.isMatchForProfileTwo()) {
            LOGGER.info("Invalid match result.");
        } else {
            LOGGER.info("Valid match result.");
        }
        return result;
    }

}
