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

    @GetMapping(path="/{matchProfileId}")
    public PupperMatcherResponse getMatches(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMatcherService.getSuccessPupperMatchResults(matchProfileId);
    }

    @PutMapping(path="/{matchProfileId}")
    public PupperMatcherResponse updateMatches(@PathVariable("matchProfileId") Long matchProfileId,
                                               @RequestBody PupperMatcherRequest pupperMatcherRequest) {
        return null;
    }

    @DeleteMapping(path="/{matchProfileId1}/match/{matchProfileId2}")
    public PupperMatcherResponse unmatchWithMatchProfile(@PathVariable("matchProfileId1") Long ativeMatchProfileId,
                                                         @PathVariable("matchProfileId2") Long passiveMatchProfileId) {
        return null;
    }

    @DeleteMapping(path="/{matchProfileId}")
    public PupperMatcherResponse deleteAllMatches(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }
}
