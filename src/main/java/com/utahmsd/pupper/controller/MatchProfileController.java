package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.MatchProfileRequest;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@Api(value = "MatchProfile Controller for MatchProfile endpoints")
public class MatchProfileController {

    @Inject
    private MatchProfileService matchProfileService;

    @RequestMapping(path="/matchProfile", method= RequestMethod.GET)
    public MatchProfileResponse getAllMatchProfiles() {
        return matchProfileService.getAllMatchProfiles();
    }

    @RequestMapping(path="/user/{userId}/matchProfile", method= RequestMethod.GET)
    public MatchProfileResponse getAllMatchProfilesByUserId(@PathVariable("userId") Long userId) {
        return matchProfileService.getMatchProfilesByUser(userId);
    }

    @RequestMapping(path="/user/{userId}/matchProfile", method= RequestMethod.POST)
    public MatchProfileResponse createMatchProfile(@PathVariable("userId") Long userId,
                                                   @RequestBody MatchProfileRequest request) {

        return matchProfileService.createMatchProfileForUser(userId, request);
    }
}
