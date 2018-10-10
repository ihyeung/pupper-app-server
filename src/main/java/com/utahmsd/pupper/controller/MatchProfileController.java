package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@Api(value = "MatchProfile Controller for MatchProfile endpoints")
public class MatchProfileController {

    private final MatchProfileService matchProfileService;

    @Inject
    MatchProfileController(MatchProfileService matchProfileService) {
        this.matchProfileService = matchProfileService;
    }

    @RequestMapping(path="/matchProfile", method= RequestMethod.GET)
    public MatchProfileResponse getAllMatchProfiles() {
        return matchProfileService.getAllMatchProfiles();
    }

    @RequestMapping(path="/user/{userId}/matchProfile", method= RequestMethod.GET)
    public MatchProfileResponse getAllMatchProfilesByUserId(@PathVariable("userId") Long userId) {
        return matchProfileService.getMatchProfilesByUser(userId);
    }

    @RequestMapping(path="/user/{userId}/matchProfile", method= RequestMethod.POST)
    public MatchProfileResponse createMatchProfile(@PathVariable("userId") Long userId) {
//        return matchProfileService.(userId);
        return null;
    }
}
