package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.util.PupperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.utahmsd.pupper.util.Constants.DEFAULT_SCORE;
import static com.utahmsd.pupper.util.PupperUtils.NOT_ACTIVE;

@Service
public class ProfileScoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileScoreService.class);
    private static final float ACTIVITY_WEIGHT = 1;
    private static final float MATCH_PERCENT_WEIGHT = 3;

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
        matchProfile.setScore(newScore < DEFAULT_SCORE ? newScore : DEFAULT_SCORE);
    }

    private float calculateScoreAdjustment(MatchProfile matchProfile) {
        float scoreAdjustment = 0;
        if (!wasRecentlyActive(matchProfile.getUserProfile().getLastLogin())) {
            scoreAdjustment -= ACTIVITY_WEIGHT;
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

        float matchPercentage = distinctMatches.size()/(completedMatchResultCount.floatValue());

        scoreAdjustment -= (matchPercentage * MATCH_PERCENT_WEIGHT);
        return scoreAdjustment;
    }

    private boolean wasRecentlyActive(Date date) {
        String lastActive = PupperUtils.lastActivityFromLastLogin(date);
        if (lastActive.equals(NOT_ACTIVE) || lastActive.contains("months")) {
            return false;
        }
        String[] stringSplit = lastActive.split(" ");
        return Integer.valueOf(stringSplit[2]) < 7 ; //Consider recent activity if lastLogin within one week ago
    }

}
