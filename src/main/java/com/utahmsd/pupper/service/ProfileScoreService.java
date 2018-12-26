package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.util.ProfileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.util.Constants.DEFAULT_MAX_SCORE;
import static com.utahmsd.pupper.util.Constants.DEFAULT_MIN_SCORE;
import static com.utahmsd.pupper.util.ProfileUtils.NOT_ACTIVE;

@Service
public class ProfileScoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileScoreService.class);

    private static final float ACTIVITY_WEIGHT = 5;
    private static final int DAYS_ACTIVE_MIN = 7;

    private static final float MATCH_PERCENT_WEIGHT = 1;
    private static final int MATCH_PERCENT_MIN = 75; //Match percent threshold for which scores should be negatively impacted

    private static final float IMAGE_WEIGHT = 1;

    private final MatchResultRepo matchResultRepo;
    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    ProfileScoreService(final MatchProfileRepo matchProfileRepo, final MatchResultRepo matchResultRepo) {
        this.matchProfileRepo = matchProfileRepo;
        this.matchResultRepo = matchResultRepo;
    }

    /**
     * Scheduled task that kicks off once daily that updates the scores of all match profiles.
     */
//    @Scheduled(cron = "0 0 0 * * *")
    private void updateScores() {
        List<MatchProfile> matchProfileList = matchProfileRepo.findAll();
        matchProfileList.forEach(this::updateProfileScoreForMatchProfile);
    }

    public void calculateMatchProfileScoreAdjustmentForMatchProfile(Long matchProfileId){
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);
        matchProfile.ifPresent(this::calculateScoreAdjustment);
    }

    private void updateProfileScoreForMatchProfile(MatchProfile matchProfile){
        float newScore = matchProfile.getScore() + calculateScoreAdjustment(matchProfile);
        matchProfile.setScore(newScore < DEFAULT_MAX_SCORE ?
                newScore > DEFAULT_MIN_SCORE ? newScore : DEFAULT_MIN_SCORE : DEFAULT_MAX_SCORE);
    }

    private float calculateScoreAdjustment(MatchProfile matchProfile) {
        float scoreAdjustment = calculateRecentActivityScoreAdjustment(matchProfile);
        LOGGER.info("User activity core adjustment: {}", scoreAdjustment);

        scoreAdjustment += calculateMatchPercentScoreAdjustment(calculateMutualMatchPercent(matchProfile));
        LOGGER.info("Match score adjustment: {}", scoreAdjustment);

        scoreAdjustment += calculateProfileImageScoreAdjustment(matchProfile);
        LOGGER.info("Profile score adjustment: {}", scoreAdjustment);

        return scoreAdjustment;
    }

    /**
     * Helper method that calculates the percentage of profiles that a given match profile liked that were
     * a mutual like.
     * @param matchProfile
     * @return
     */
    private float calculateMutualMatchPercent(MatchProfile matchProfile) {
        Integer likesForMatchProfile = matchResultRepo.getLikesByMatchProfileId(matchProfile.getId());
        Integer mutualMatches = matchResultRepo.getMutualMatchesCount(matchProfile.getId());

        return mutualMatches/likesForMatchProfile.floatValue();
    }

    private float calculateMatchPercentScoreAdjustment(float mutualMatchPercentage) {
        float matchPercentAdjustment = 1/mutualMatchPercentage * MATCH_PERCENT_WEIGHT;
        return mutualMatchPercentage >= MATCH_PERCENT_MIN ?
                matchPercentAdjustment : -1 * matchPercentAdjustment;
    }

    private float calculateRecentActivityScoreAdjustment(MatchProfile matchProfile) {
        if (!wasRecentlyActive(matchProfile.getUserProfile().getLastLogin())) {
            return  -1 * ACTIVITY_WEIGHT/ACTIVITY_WEIGHT;  //Decrease magnitude of score decrease from inactivity
        }
            return ACTIVITY_WEIGHT; //Increase magnitude of score increase from recent activity
    }

    private float calculateProfileImageScoreAdjustment(MatchProfile matchProfile) {
        return null == matchProfile.getProfileImage() ? -1 * IMAGE_WEIGHT : IMAGE_WEIGHT * 2;
    }

    private boolean wasRecentlyActive(Date date) {
        String lastActive = ProfileUtils.lastActivityFromLastLogin(date);
        if (lastActive.equals(NOT_ACTIVE) || lastActive.contains("months")) {
            return false;
        }
        String[] stringSplit = lastActive.split(" ");
        return Integer.valueOf(stringSplit[2]) <= DAYS_ACTIVE_MIN ; //Consider recent activity if lastLogin within one week ago
    }

}
