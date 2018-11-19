package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.PupperMatcherRequest;
import com.utahmsd.pupper.dto.PupperMatcherResponse;
import com.utahmsd.pupper.service.PupperMatcherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for mobile app's main matching/playing functionality through calling methods contained in PupperMatcherService.
 * A user flips through various MatchProfiles "cards" within a deck and selects like/dislike on each MatchProfile card.
 */

@RestController
@Api(value = "PupperMatcher Controller For PupperMatcherService Endpoints")
@RequestMapping("/match")
public class PupperMatcherController {

    private final PupperMatcherService pupperMatcherService;

    @Autowired
    public PupperMatcherController(PupperMatcherService pupperMatcherService) {
        this.pupperMatcherService = pupperMatcherService;
    }


    @GetMapping(path="/{matchProfileId}/play")
    public PupperMatcherResponse getNewBatchOfMatchProfiles(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }

    /*
    Endpoint that retrieves a list of all matchProfiles that a matchProfileId has mutually matched with.
     */
    @GetMapping(path="/{matchProfileId}")
    public PupperMatcherResponse getMatches(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMatcherService.getSuccessPupperMatchResults(matchProfileId);
    }

    /*
    Explicit endpoint to see whether a match result record exists between a pair of match profiles.
     */
    @GetMapping(path="/{matchProfileId1}/match/{matchProfileId2}")
    public PupperMatcherResponse findMatchResultByIds(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                     @PathVariable("matchProfileId2") Long matchProfileId2) {
        return null;
    }

    /*
    Main endpoint that gets hit for every result/outcome for any given user using the app's main matching/playing functionality.
     */
    @PutMapping(path="/{matchProfileId1}/match/{matchProfileId2}")
    public PupperMatcherResponse createOrUpdateMatchResult(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                           @PathVariable("matchProfileId2") Long matchProfileId2,
                                                           @RequestBody PupperMatcherRequest pupperMatcherRequest) {
        return null;
        //TODO: service method that queries for an existing match result record between two users.
        //TODO: If a match result record does not exist, insert new record into match_result table
        //TODO: If a record exists between the two match profiles, update existing record.
    }

    /*
    Helper endpoint for updating multiple match result data records for a given matchProfileId.
    May use this later to batch updates to database to improve performance.
     */
    @PutMapping(path="/{matchProfileId}")
    public PupperMatcherResponse updateMatchResultData(@PathVariable("matchProfileId") Long matchProfileId,
                                                       @RequestBody PupperMatcherRequest pupperMatcherRequest) {
        return null;
    }

    /*
    Delete endpoint that updates an existing matchResult record to unmatch with a matchProfileId2 for a given matchProfileId1.
    I.e., either match_profile_1_result or match_profile_2_result is updated from true to false.
    This will remove the ability for messages to be exchanged between the two users.

    This endpoint will likely be hit together with a call to the delete endpoint in MessageController
    to delete all messages exchanged between the two users.
     */
    @DeleteMapping(path="/{matchProfileId1}/match/{matchProfileId2}")
    public PupperMatcherResponse unmatchWithMatchProfile(@PathVariable("matchProfileId1") Long ativeMatchProfileId,
                                                         @PathVariable("matchProfileId2") Long passiveMatchProfileId) {
        return null;
    }

    /*
    Delete endpoint that should be used when a matchProfile or userProfile is deleted, which will remove all matchResult records
    corresponding to a given matchProfileId.

    This endpoint will likely be hit together with a call to the delete endpoint in MessageController that deletes all
    messages sent/received by a given matchProfileId.
     */
    @DeleteMapping(path="/{matchProfileId}")
    public PupperMatcherResponse deleteAllMatches(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }
}
