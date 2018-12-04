package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dto.MatcherDataRequest;
import com.utahmsd.pupper.dto.MatcherDataResponse;
import com.utahmsd.pupper.dto.MatcherResponse;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import com.utahmsd.pupper.service.MatcherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for mobile app's main matching/playing functionality through calling methods contained in PupperMatcherService.
 * A user flips through various MatchProfiles "cards" within a deck and selects like/dislike on each MatchProfile card.
 */

@RestController
@Api(value = "PupperMatcher Controller For PupperMatcherService Endpoints")
@RequestMapping("/matcher")
public class MatcherController {

    private final MatcherService matcherService;

    @Autowired
    public MatcherController(MatcherService matcherService) {
        this.matcherService = matcherService;
    }

    /**
     * Retrieves ALL unseen match profiles for a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @GetMapping(path="/all", params = {"matchProfileId"})
    public List<MatchProfile> getAllUnseenProfilesForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId) {
        return matcherService.getAllUnseenMatchProfilesForMatchProfile(matchProfileId);
    }
    /**
     * Retrieves next batch of profile cards to display to the user.
     * @param matchProfileId
     * @return
     */
    @GetMapping(params = {"matchProfileId", "zipRadius"})
    public List<ProfileCard> fetchMatcherDataBatch(@RequestParam("matchProfileId") Long matchProfileId,
                                                   @RequestParam("zipRadius") int zipRadius) {
        return matcherService.retrieveMatcherDataProfileCards(matchProfileId, zipRadius);
    }

    /**
     * Helper endpoint for updating multiple match result data records for a given matchProfileId.
     * May use this later to batch updates to database to improve performance.
     * @param matchProfileId
     * @param matcherRequest
     * @return
     */
    @PutMapping(params = {"matchProfileId"})
    public MatcherDataResponse submitMatcherResults(@RequestParam("matchProfileId") Long matchProfileId,
                                                    @RequestBody MatcherDataRequest matcherRequest) {
        return null;
    }

    /**
     * Retrieves all matchResult data relating to a given matchProfileId.
     * @param matchProfileId
     * @param getSuccessResultData boolean flag that can be optionally set to filter to retrieve only mutual matches.
     * @return
     */
    @GetMapping(params = {"matchProfileId", "isSuccess"})
    public MatcherResponse getMatchResultDataForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId,
                                                @RequestParam("isSuccess") boolean getSuccessResultData) {

        return null;
    }

    /**
     * Explicit crud endpoint to retrieve a matcher result record between a pair of match profiles.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @GetMapping(params = {"matchProfileId1", "matchProfileId2"})
    public MatchResult findMatcherResultById(@RequestParam("matchProfileId1") Long matchProfileId1,
                                             @RequestParam("matchProfileId2") Long matchProfileId2) {
        return matcherService.checkForMatch(matchProfileId1, matchProfileId2);
    }

    /**
     *  Endpoint that gets hit for a single matcherResult for any given user using the app's main matching/playing functionality.
     *
     * Also may be used when a user decides to click unmatch with a previously established matchResult.
     * @param matchProfileId1
     * @param matchProfileId2
     * @param isMatchForMatchProfileId1
     * @return
     */
    @PutMapping(params = {"matchProfileId", "resultFor", "isMatch"})
    public MatcherResponse createOrUpdateMatcherResult(@RequestParam("matchProfileId") Long matchProfileId1,
                                                       @RequestParam("resultFor") Long matchProfileId2,
                                                       @RequestParam("isMatch") boolean isMatchForMatchProfileId1) {
        return null;
        //TODO: service method that queries for an existing match result record between two users.
        //TODO: If a match result record does not exist, insert new record into match_result table
        //TODO: If a record exists between the two match profiles, update existing record.
    }

    /**
     * Delete endpoint that should be used when a matchProfile or userProfile is deleted, which will remove all matchResult records
     * corresponding to a given matchProfileId.
     *
     * This endpoint will likely be hit together with a call to the delete endpoint in MessageController that deletes all
     * messages sent/received by a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @DeleteMapping(params = {"matchProfileId"})
    public MatcherResponse deleteAllMatcherResultDataForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId) {
        return null;
    }
}
