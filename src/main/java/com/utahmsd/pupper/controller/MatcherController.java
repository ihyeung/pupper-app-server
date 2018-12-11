package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dto.MatcherDataRequest;
import com.utahmsd.pupper.dto.MatcherDataResponse;
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
     * Helper endpoint for updating a batch of multiple match result data records for a given matchProfileId.
     * May use this later to batch updates to database to improve performance.
     * @param matchProfileId
     * @param matcherRequest
     * @return
     */
    @PostMapping(path = "/result/submit", params = {"matchProfileId"})
    public MatcherDataResponse submitCompletedMatcherResults(@RequestParam("matchProfileId") Long matchProfileId,
                                                             @RequestBody MatcherDataRequest matcherRequest) {
        return matcherService.updateMatcherResults(matchProfileId, matcherRequest);
    }

    /**
     * Explicit crud endpoint to retrieve a match result record between a pair of match profiles.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @GetMapping(path = "/result", params = {"matchProfileId1", "matchProfileId2"})
    public MatchResult getMatchResultForMatchProfiles(@RequestParam("matchProfileId1") Long matchProfileId1,
                                                      @RequestParam("matchProfileId2") Long matchProfileId2) {
        return matcherService.checkForTwoWayMatch(matchProfileId1, matchProfileId2);
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
    @PostMapping(path = "/result", params = {"matchProfileId", "resultFor", "isMatch"})
    public void createOrUpdateMatcherResult(@RequestParam("matchProfileId") Long matchProfileId1,
                                            @RequestParam("resultFor") Long matchProfileId2,
                                            @RequestParam("isMatch") Boolean isMatchForMatchProfileId1) {
        matcherService.insertOrUpdateMatchResult(matchProfileId1, matchProfileId2, isMatchForMatchProfileId1);
    }

    /**
     * Delete endpoint that should be used when a matchProfile or userProfile is deleted, which will remove all matchResult records
     * corresponding to a given matchProfileId.
     * This endpoint will likely be hit together with a call to the delete endpoint in MessageController that deletes all
     * messages sent/received by a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @DeleteMapping(path = "/result", params = {"matchProfileId"})
    public void deleteAllMatchResultsForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId) {
        matcherService.deleteAllMatchResultDataByMatchProfileId(matchProfileId);
    }

    /**
     * Retrieves all matchResult completed records relating to a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @GetMapping(path = "/result", params = {"matchProfileId"})
    public List<MatchResult> getMatchResultDataForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId) {
        return matcherService.retrieveMatchResultDataForMatchProfile(matchProfileId);
    }

    /**
     * Resets a specific match result record between two match profiles.
     *
     * This endpoint should be used when a given match profile wants to un-match with a previously matched match profile.
     * @param matchProfileId match profile one
     * @param matchProfileToUnmatchWith match profile two
     */
    @PostMapping(path = "/result", params = {"matchProfileId", "unmatchWith"})
    public void unmatchWithMatchProfileByMatchProfileIds(@RequestParam("matchProfileId") Long matchProfileId,
                                                          @RequestParam("unmatchWith") Long matchProfileToUnmatchWith) {
        matcherService.unmatchWithMatchProfile(matchProfileId, matchProfileToUnmatchWith);
    }

    /**
     * Deletes a specific match result record between two match profiles.
     * @param matchProfileId match profile one
     * @param matchProfileId2 match profile two
     */
    @DeleteMapping(path = "/result", params = {"matchProfileId1", "matchProfileId2"})
    public void deleteMatchResultForMatchProfileOneAndMatchProfileTwo(@RequestParam("matchProfileId1") Long matchProfileId,
                                                                      @RequestParam("matchProfileId2") Long matchProfileId2) {
        matcherService.deleteMatchResultRecordForMatchProfiles(matchProfileId, matchProfileId2);
    }

    /**
     * IGNORE ME, this is a testing method in an attempt to resolve a bug!
     */
    @PostMapping(path = "/expiresAt")
    public void updateExpiresAt() {
        matcherService.updateRecordExpiresForAllFields();
    }
}
