package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.PupperMatcherResponse;
import com.utahmsd.pupper.service.PupperMatcherService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;


/**
 * Controller for PupperMatcherService where a user is shown various MatchProfiles
 */

@RestController
@Api(value = "PupperMatcher Controller For PupperMatcherService Endpoints")
@RequestMapping("/matcher")
public class PupperMatcherController {

    @Inject
    private PupperMatcherService pupperMatcherService;

    @RequestMapping(path="/{matchProfileId}", method= RequestMethod.GET)
    public PupperMatcherResponse getMatches(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMatcherService.getSuccessPupperMatchResults(matchProfileId);
    }
}
