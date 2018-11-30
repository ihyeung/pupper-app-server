package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import com.utahmsd.pupper.service.filter.MatchProfileFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@Api(value = "MatchProfile Controller for MatchProfile endpoints")
public class MatchProfileController {

    private final MatchProfileService matchProfileService;
    private final MatchProfileFilterService matchProfileFilterService;

    @Autowired
    MatchProfileController(MatchProfileService matchProfileService, MatchProfileFilterService matchProfileFilterService) {
        this.matchProfileService = matchProfileService;
        this.matchProfileFilterService = matchProfileFilterService;
    }

    @GetMapping(path="/matchProfile")
    public MatchProfileResponse getAllMatchProfiles() {
        return matchProfileService.getAllMatchProfiles();
    }

    @GetMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse getMatchProfilesByUserId(@PathVariable("userId") Long userId) {
        return matchProfileService.getAllMatchProfilesByUserId(userId);
    }

    @GetMapping(path="/matchProfile", params = {"userEmail"})
    public MatchProfileResponse getMatchProfilesByUserEmail(@RequestParam("userEmail") @Email String email) {
        return matchProfileFilterService.getAllMatchProfilesByEmail(email);
    }

    @GetMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public MatchProfileResponse getMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userProfileId,
                                                                                @PathVariable("matchProfileId") Long matchProfileId) {
        return matchProfileService.getMatchProfileById(userProfileId, matchProfileId);
    }

    @PostMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse createMatchProfileForUserByUserProfileId(@PathVariable("userId") Long userId,
                                                                        @RequestBody @Valid MatchProfile matchProfile) {

        return matchProfileService.createOrUpdateMatchProfileForUser(userId, matchProfile);
    }

    @PostMapping(path="/user/{userId}/matchProfile/{matchProfileId}", params = {"profilePic"})
    public MatchProfileResponse updateProfileImageForMatchProfile(@PathVariable("userId") Long userId,
                                                                 @PathVariable("matchProfileId") Long matchProfileId,
                                                                 @RequestParam("profilePic") String profilePic) {

        return matchProfileService.updateProfileImageByMatchProfileId(userId, matchProfileId, profilePic);
    }

    @PutMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public MatchProfileResponse updateMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                                   @PathVariable("matchProfileId") Long matchProfileId,
                                                                                    @RequestBody @Valid MatchProfile matchProfile) {
        return matchProfileService.updateMatchProfile(userId, matchProfileId, matchProfile);
    }

    @DeleteMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public MatchProfileResponse deleteMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                                   @PathVariable("matchProfileId") Long matchProfileId) {
        return matchProfileService.deleteMatchProfile(userId, matchProfileId);
    }

    @DeleteMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse deleteMatchProfilesByUserProfileId(@PathVariable("userId") Long userId) {
        return matchProfileService.deleteMatchProfilesByUserId(userId);
    }
}
