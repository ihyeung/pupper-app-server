package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.util.PupperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.utahmsd.pupper.util.Constants.DEFAULT_MAX_SCORE;
import static com.utahmsd.pupper.util.PupperUtils.NOT_ACTIVE;

@Service
public class ProfileScoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileScoreService.class);

    private static final float ACTIVITY_WEIGHT = 1;
    private static final int DAYS_ACTIVE_MIN = 7;

    private static final float MATCH_PERCENT_WEIGHT = 3;
    private static final int MATCH_PERCENT_MIN = 80;

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

    public void getMatchProfileScoreById(Long matchProfileId){
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);
        matchProfile.ifPresent(this::calculateScoreAdjustment);
    }

    private void updateProfileScoreForMatchProfile(MatchProfile matchProfile){
        float newScore = matchProfile.getScore() + calculateScoreAdjustment(matchProfile);
        matchProfile.setScore(newScore < DEFAULT_MAX_SCORE ? newScore : DEFAULT_MAX_SCORE);
    }

    private float calculateScoreAdjustment(MatchProfile matchProfile) {
        float scoreAdjustment = 0;
        if (!wasRecentlyActive(matchProfile.getUserProfile().getLastLogin())) {
            scoreAdjustment -= ACTIVITY_WEIGHT;
        } else {
            scoreAdjustment += ACTIVITY_WEIGHT;
        }
        Integer completedMatchResultCount = matchResultRepo.getCompletedMatchResultCount(matchProfile.getId());
        if (completedMatchResultCount == 0) {
            return scoreAdjustment;
        }
        //FIXME: Add a repo method that only queries for the count, not a list of matchProfiles
        List<MatchProfile> matches1 = matchResultRepo.findActiveMatches(matchProfile.getId());
        List<MatchProfile> matches2 = matchResultRepo.findPassiveMatches(matchProfile.getId());
        Set<MatchProfile> distinctMatches = new HashSet<>();
        distinctMatches.addAll(matches1);
        distinctMatches.addAll(matches2);

        Integer mutualMatches = matchResultRepo.getMutualMatchesCount(matchProfile.getId());

        assert(mutualMatches == distinctMatches.size());

        float matchPercentage = distinctMatches.size()/(completedMatchResultCount.floatValue());
        LOGGER.info("Match percent: {}", matchPercentage);

        if (matchPercentage >= MATCH_PERCENT_MIN) {
            scoreAdjustment += (1/matchPercentage * MATCH_PERCENT_WEIGHT);
        } else {
            scoreAdjustment -= (1/matchPercentage * MATCH_PERCENT_WEIGHT);

        }
        LOGGER.info("Score adjustment: {}", scoreAdjustment);
        return scoreAdjustment;
    }

    private boolean wasRecentlyActive(Date date) {
        String lastActive = PupperUtils.lastActivityFromLastLogin(date);
        if (lastActive.equals(NOT_ACTIVE) || lastActive.contains("months")) {
            return false;
        }
        String[] stringSplit = lastActive.split(" ");
        return Integer.valueOf(stringSplit[2]) <= DAYS_ACTIVE_MIN ; //Consider recent activity if lastLogin within one week ago
    }

}
