package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.MatcherDataRequest;
import com.utahmsd.pupper.dto.MatcherDataResponse;
import com.utahmsd.pupper.dto.MatcherRequest;
import com.utahmsd.pupper.dto.MatcherResponse;
import com.utahmsd.pupper.service.MatcherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /*
    Retrieves next batch of profile cards to display to the user.
     */
    @GetMapping(path="/matchProfile/{matchProfileId}")
    public MatcherDataResponse fetchMatcherData(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }

    /*
    Helper endpoint for updating multiple match result data records for a given matchProfileId.
    May use this later to batch updates to database to improve performance.
     */
    @PutMapping(path="/matchProfile/{matchProfileId}")
    public MatcherDataResponse submitMatcherResults(@PathVariable("matchProfileId") Long matchProfileId,
                                                    @RequestBody MatcherDataRequest matcherRequest) {
        return null;
    }

    /*
    Endpoint that retrieves a list of all matchProfiles that a matchProfileId has mutually matched with.
     */
    @GetMapping(path="/result/matchProfile/{matchProfileId}", params = {"isSuccess"})
    public MatcherResponse getMatcherResultData(@PathVariable("matchProfileId") Long matchProfileId,
                                              @RequestParam("isSuccess") boolean getSuccessResultData) {

        return matcherService.getPupperMatcherResults(matchProfileId, getSuccessResultData);
    }

    /**
     * Explicit crud endpoint to retrieve a matcher result record between a pair of match profiles.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @GetMapping(path="/result/matchProfile/{matchProfileId1}", params = {"resultFor"})
    public MatcherResponse findMatcherResultById(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                @RequestParam("resultFor") Long matchProfileId2) {
        return null;
    }

    /*
     Endpoint that gets hit for a single matcherResult for any given user using the app's main matching/playing functionality.

    Also will be used when a user decides to click unmatch with a previously established matchResult.
     */
    @PutMapping(path="/result/matchProfile/{matchProfileId1}", params = {"resultFor", "isMatch"})
    public MatcherResponse createOrUpdateMatcherResult(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                       @RequestParam("resultFor") Long matchProfileId2,
                                                       @RequestParam("isMatch") boolean isMatchForMatchProfileId1) {
        return null;
        //TODO: service method that queries for an existing match result record between two users.
        //TODO: If a match result record does not exist, insert new record into match_result table
        //TODO: If a record exists between the two match profiles, update existing record.
    }

    /*
    Delete endpoint that should be used when a matchProfile or userProfile is deleted, which will remove all matchResult records
    corresponding to a given matchProfileId.

    This endpoint will likely be hit together with a call to the delete endpoint in MessageController that deletes all
    messages sent/received by a given matchProfileId.
     */
    @DeleteMapping(path="/result/matchProfile/{matchProfileId}")
    public MatcherResponse deleteAllMatcherResultData(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }
}
