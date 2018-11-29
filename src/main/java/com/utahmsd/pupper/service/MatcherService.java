package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.MatcherRequest;
import com.utahmsd.pupper.dto.MatcherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class MatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final int DEFAULT_BATCH_SIZE = 50; //Number of match profiles to retrieve for a user per batch


    private final MatchProfileRepo matchProfileRepo;
    private final PupperProfileRepo pupperProfileRepo;
    private final UserProfileRepo userProfileRepo;
    private final MatchResultRepo matchResultRepo;

    @Autowired
    public MatcherService(UserProfileRepo userProfileRepo, PupperProfileRepo pupperProfileRepo,
                          MatchProfileRepo matchProfileRepo, MatchResultRepo matchResultRepo) {
        this.matchProfileRepo = matchProfileRepo;
        this.pupperProfileRepo = pupperProfileRepo;
        this.userProfileRepo = userProfileRepo;
        this.matchResultRepo = matchResultRepo;
    }

    //Called when #1 swipes left/right on #2 and #2 has not rated #1's profile yet
    public MatcherResponse insertNewMatchResult(MatcherRequest request) { return null;}

    //Called when #2 swipes left/right on #1 (Someone swipes back on someone who has already swiped/rated them)
    public MatcherResponse updateMatchResult(MatcherRequest request) { return null;}

    public MatcherResponse getPupperMatcherResults(Long matchProfileId, boolean getSuccessResults) { //get list of match profiles for a given match profile where both parties rated each other positively
        return null;
    }

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


}
