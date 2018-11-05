package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "MatchProfile Controller for MatchProfile endpoints")
public class MatchProfileController {

    private final MatchProfileService matchProfileService;

    @Autowired
    MatchProfileController(MatchProfileService matchProfileService) {
        this.matchProfileService = matchProfileService;
    }

    @GetMapping(path="/matchProfile")
    public MatchProfileResponse getAllMatchProfiles() {
        return matchProfileService.getAllMatchProfiles();
    }

    @GetMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse getAllMatchProfilesByUserId(@PathVariable("userId") Long userId) {
        return matchProfileService.getAllMatchProfilesByUserId(userId);
    }

    @PostMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse createMatchProfile(@PathVariable("userId") Long userId,
                                                   @RequestBody @Valid final MatchProfile matchProfile) {

        return matchProfileService.createMatchProfileForUser(userId, matchProfile);
    }
}
