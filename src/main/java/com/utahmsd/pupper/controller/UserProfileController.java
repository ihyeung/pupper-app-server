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
        return userProfileService.getAllUserProfiles(sort, limit);
    }

    @GetMapping(path ="/{userId}")
    public UserProfileResponse findUserProfileById(@PathVariable("userId") Long userId) {
        return userProfileService.findUserProfileById(userId);
    }

    @GetMapping(params = {"email"})
    public UserProfileResponse findUserProfileByUserAccountEmail(@RequestParam(value = "email", required = false) String email) {
        return userProfileService.findUserProfileByUserAccountEmail(email);
    }

    @PostMapping()
    public UserProfileResponse createUserProfile(@RequestBody @Valid final UserProfile userProfile) {
        return userProfileService.createNewUserProfile(userProfile);
    }

    @PutMapping(path ="/{userId}")
    public UserProfileResponse updateUserProfile(@PathVariable("userId") Long userId,
                                                 @RequestBody @Valid final UserProfile userProfile) {
        return userProfileService.updateUserProfile(userId, userProfile);
    }

    @PutMapping(path = "/{userId}", params = {"lastLogin"})
    public UserProfileResponse updateLastLogin(@PathVariable("userId") Long userId,
                                               @RequestParam(value = "lastLogin") String lastLogin) {
        return userProfileService.updateLastLoginForUserProfile(userId, lastLogin);
    }

    /*
    Delete endpoint for deleting a userProfile.

    This endpoint will only ever be called when coupled with the following calls (in order of occurrence):
    -
    -AuthController deleteAccount endpoint to delete the UserAccount
     */
    @DeleteMapping(path ="/{userId}")
    public UserProfileResponse deleteUserProfileById(@PathVariable("userId") Long userId) {
        return userProfileService.deleteUserProfileById(userId);
    }

    //Search result query filter endpoints

    @GetMapping(params = {"zip"})
    public UserProfileResponse getUserProfilesWithZip(@RequestParam(value = "zip") String zip) {
        return userProfileFilterService.filterUserProfilesByZip(zip);
    }

}
