package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.service.UserProfileService;
import com.utahmsd.pupper.service.filter.UserProfileFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "UserProfile Controller For User Endpoints")
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserProfileFilterService userProfileFilterService;

    @Autowired
    UserProfileController(UserProfileService userProfileService, UserProfileFilterService userProfileFilterService) {
        this.userProfileService = userProfileService;
        this.userProfileFilterService = userProfileFilterService;
    }

    @GetMapping(params = {"sortBy", "limit"})
    public UserProfileResponse getAllUserProfiles(@RequestParam(value = "sortBy") String sort,
                                                  @RequestParam(value = "limit") String limit) {
        return userProfileFilterService.getUserProfilesWithFilters(sort, limit);
    }

    @GetMapping(path = "/{userId}")
    public UserProfileResponse findUserProfileById(@PathVariable("userId") Long userProfileId) {
        return userProfileService.findUserProfileById(userProfileId);
    }

    @PostMapping()
    public UserProfileResponse createOrInsertUserProfile(@RequestBody @Valid UserProfile userProfile) {
        return userProfileService.createOrUpdateUserProfile(userProfile);
    }

    @PutMapping(path = "/{userId}")
    public UserProfileResponse updateUserProfileById(@PathVariable("userId") Long userProfileId,
                                                     @RequestBody @Valid UserProfile userProfile) {
        return userProfileService.updateUserProfileByUserProfileId(userProfileId, userProfile);
    }

    @DeleteMapping(path = "/{userId}")
    public UserProfileResponse deleteUserProfileById(@PathVariable("userId") Long userProfileId) {
        return userProfileService.deleteUserProfileById(userProfileId);
    }

    @GetMapping(params = {"zip"})
    public UserProfileResponse getUserProfilesWithZip(@RequestParam(value = "zip") String zip) {
        return userProfileFilterService.getUserProfilesFilterByZip(zip);
    }

    @GetMapping(params = {"email"})
    public UserProfileResponse findUserProfileByUserAccountEmail(@RequestParam(value = "email") String email) {
        return userProfileFilterService.getUserProfileFilterByEmail(email);
    }

    @PutMapping(params = {"email"})
    public UserProfileResponse updateUserProfileByUserAccountEmail(@RequestParam(value = "email") String email,
                                                                   @RequestBody @Valid UserProfile userProfile) {
        return userProfileFilterService.updateUserProfileFilterByEmail(email, userProfile);
    }

    @PutMapping(path = "/{userId}", params = {"lastLogin"})
    public UserProfileResponse updateLastLogin(@PathVariable("userId") Long userProfileId,
                                               @RequestParam(value = "lastLogin") String lastLogin) {
        return userProfileService.updateLastLoginForUserProfile(userProfileId, lastLogin);
    }

    @PostMapping(path="/{userId}", params = {"profilePic"})
    public UserProfileResponse updateProfileImageForMatchProfile(@PathVariable("userId") Long userId,
                                                                  @RequestParam("profilePic") String profilePic) {
        return userProfileService.updateProfileImageById(userId, profilePic);
    }

    @DeleteMapping()
    public void deleteUserProfileByEmail(@RequestParam("email") String email) {
        userProfileService.deleteUserProfileByEmail(email);
    }

}
