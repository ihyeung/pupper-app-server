package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import com.utahmsd.pupper.service.ProfileScoreService;
import com.utahmsd.pupper.service.filter.MatchProfileFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@Api(value = "MatchProfile Controller for MatchProfile endpoints")
public class MatchProfileController {

    private final MatchProfileService matchProfileService;
    private final MatchProfileFilterService matchProfileFilterService;
    private final ProfileScoreService profileScoreService;

    @Autowired
    MatchProfileController(MatchProfileService matchProfileService, MatchProfileFilterService matchProfileFilterService,
                           ProfileScoreService profileScoreService) {
        this.matchProfileService = matchProfileService;
        this.matchProfileFilterService = matchProfileFilterService;
        this.profileScoreService = profileScoreService;
    }

    @GetMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse getMatchProfilesByUserProfileId(@PathVariable("userId") Long userId) {
        return matchProfileService.getAllMatchProfilesByUserId(userId);
    }

    @GetMapping(path="/matchProfile", params = {"matchProfileId"})
    public MatchProfile getMatchProfileByMatchProfileId(@RequestParam("matchProfileId") Long matchProfileId) {
        return matchProfileService.getMatchProfileByMatchProfileId(matchProfileId);
    }

    @GetMapping(path="/matches", params = {"matchProfileId"})
    public List<MatchProfile> retrieveMatchesForMatchProfile(@RequestParam("matchProfileId") Long matchProfileId) {
        return matchProfileService.retrieveMatchesForMatchProfile(matchProfileId);
    }

    @PostMapping(path="/user/{userId}/matchProfile")
    public MatchProfileResponse createMatchProfileForUserByUserProfileId(@PathVariable("userId") Long userId,
                                                                         @RequestBody @Valid MatchProfile matchProfile) {

        return matchProfileService.createMatchProfileForUser(userId, matchProfile);
    }

    @PostMapping(path="/user/{userId}/matchProfile/{matchProfileId}", params = {"profilePic"}) //PostMapping to be consistent with implementation in AmazonAwsClient
    public MatchProfileResponse updateProfileImageForMatchProfile(@PathVariable("userId") Long userId,
                                                                 @PathVariable("matchProfileId") Long matchProfileId,
                                                                 @RequestParam("profilePic") String profilePic) {

        return matchProfileService.updateProfileImageByMatchProfileId(userId, matchProfileId, profilePic);
    }

    @PutMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public MatchProfileResponse updateMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                                   @PathVariable("matchProfileId") Long matchProfileId,
                                                                                    @RequestBody @Valid MatchProfile matchProfile) {
        return matchProfileService.updateMatchProfileByUserIdAndMatchProfileId(userId, matchProfileId, matchProfile);
    }

    @DeleteMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public void deleteMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                   @PathVariable("matchProfileId") Long matchProfileId) {
        matchProfileService.deleteMatchProfile(userId, matchProfileId);
    }

    @DeleteMapping(path="/matchProfile", params = {"userId"})
    public MatchProfileResponse deleteMatchProfilesByUserProfileId(@RequestParam("userId") Long userId) {
        return matchProfileService.deleteMatchProfilesByUserProfileId(userId);
    }


    ///////////////////////// ENDPOINTS FOR TESTING/ETC (NOT EXPLICITLY USED BY CLIENT) //////////////////////////////////

    @GetMapping(path="/matchProfile")
    public MatchProfileResponse getAllMatchProfiles() {
        return matchProfileService.getAllMatchProfiles();
    }

    @GetMapping(path="/matchProfile", params = {"sortBy", "limit"})
    public List<MatchProfile> getAllMatchProfilesWithFilters(@RequestParam("sortBy") String sort, @RequestParam("limit") String limit) {
        return matchProfileFilterService.getMatchProfilesWithLastLoginSortLimitFilters(sort, limit);
    }

    @GetMapping(path="/user/{userId}/matchProfile/{matchProfileId}")
    public MatchProfileResponse getMatchProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userProfileId,
                                                                                @PathVariable("matchProfileId") Long matchProfileId) {
        return matchProfileService.getMatchProfileByIds(userProfileId, matchProfileId);
    }

    @GetMapping(path="/matchProfile", params = {"userEmail"})
    public List<MatchProfile> getMatchProfilesFilterByUserEmail(@RequestParam("userEmail") @Email String email) {
        return matchProfileFilterService.getMatchProfilesFilterByUserEmail(email);
    }

    @GetMapping(path="/matchProfile", params = {"zip"})
    public List<MatchProfile> getMatchProfilesFilterByUserZip(@RequestParam("zip") String zipcode) {
        return matchProfileFilterService.getMatchProfilesFilterByUserProfileZip(zipcode);
    }

    @GetMapping(path="/matchProfile", params = {"breed"})
    public List<MatchProfile> getMatchProfilesFilterByBreed(@RequestParam("breed") String breed) {
        return matchProfileFilterService.getMatchProfilesFilterByBreed(breed);
    }

    @GetMapping(path="/matchProfile/score", params = {"matchProfileId"})
    public void getMatchProfileScoreAdjustment(@RequestParam("matchProfileId") Long matchProfileId) {
        profileScoreService.calculateMatchProfileScoreAdjustmentForMatchProfile(matchProfileId);
    }
}
