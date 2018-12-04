package com.utahmsd.pupper.service;

import com.utahmsd.pupper.client.ZipCodeAPIClient;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dto.MatcherRequest;
import com.utahmsd.pupper.dto.MatcherResponse;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import com.utahmsd.pupper.util.ZipcodeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.utahmsd.pupper.client.ZipCodeAPIClient.MAX_RADIUS;

@Named
@Singleton
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_BATCH_SIZE = 3; //Number of match profiles to retrieve for a user per batch
    private static final int DEFAULT_ZIP_RADIUS = 3;


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
            List<String> zipcodesInRange = zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), zipRadius);
            List<Long> idList = new ArrayList<>();
            getPastMatcherResultsForMatchProfile(matchProfileId)
                    .forEach(each -> idList.add(each.getId()));

            List<MatchProfile> profiles =
                    matchProfileRepo.findAllByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId, zipcodesInRange);
            if (!idList.isEmpty()) {
                profiles = matchProfileRepo.
                        findAllByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(idList, matchProfileId, zipcodesInRange);
            }
            LOGGER.info("Number of unseen profiles within zip code radius: {}", profiles.size());

            return profiles;
        }
        return null;
    }

    public List<MatchProfile> getNextMatchProfileBatchWithZipFilter(Long matchProfileId, int radius) {
        Optional<MatchProfile> m = matchProfileRepo.findById(matchProfileId);
        int zipRadius = radius > 0 && radius < MAX_RADIUS ? radius : DEFAULT_ZIP_RADIUS;
        if (m.isPresent()) {
            List<String> zipcodesInRange = zipCodeAPIClient.getZipCodesInRadius(m.get().getUserProfile().getZip(), zipRadius);
            LOGGER.info("Number of zipcodes in range of {}: {}", m.get().getUserProfile().getZip(), zipcodesInRange.size());

            List<Long> idList = new ArrayList<>();
            getPastMatcherResultsForMatchProfile(matchProfileId)
                    .forEach(each -> idList.add(each.getId()));
            LOGGER.info("Number of ids to exclude: {}", idList.size());

            List<MatchProfile> profiles =
                    matchProfileRepo.findTop5ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(matchProfileId, zipcodesInRange);
            if (!idList.isEmpty()) {
                profiles = matchProfileRepo.
                        findTop5ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(idList, matchProfileId, zipcodesInRange);
            }
            LOGGER.info("Number of unseen profiles within zip code radius: {}", profiles.size());

            return profiles;
        }
        return null;
    }





    //Called when #1 swipes left/right on #2 and #2 has not rated #1's profile yet
    public MatcherResponse insertNewMatchResult(MatcherRequest request) { return null;}

    //Called when #2 swipes left/right on #1 (Someone swipes back on someone who has already swiped/rated them)
    public MatcherResponse updateMatchResult(MatcherRequest request) { return null;}

    public MatcherResponse getPastPupperMatchResults(Long matchProfileId) { //All match profiles that a given match profile has been shown and rated (i.e., pastPupperMatches = success pupper matches + failure pupper matches)
        return null;
    }

    public MatcherResponse getNewPupperMatchResultsBatch(Long matchProfileId) { //Pulls next batch of match profiles that will be shown to user
        return null;
    }

    public MatcherResponse updatePupperMatchResultsForBatch (MatcherRequest request) {// Insert or update match result data for records in a batch
        return null;
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
