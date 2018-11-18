package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.service.UserProfileService;
import com.utahmsd.pupper.service.filter.UserSearchFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "UserProfile Controller For User Endpoints")
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserSearchFilterService userSearchFilterService;

    @Autowired
    UserProfileController(UserProfileService userProfileService, UserSearchFilterService userSearchFilterService) {
        this.userProfileService = userProfileService;
        this.userSearchFilterService = userSearchFilterService;
    }

    @GetMapping()
    public UserProfileResponse getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping(path ="/{userId}")
    public UserProfileResponse findUserProfileById(@PathVariable("userId") Long userId) {
        return userProfileService.findUserProfileById(userId);
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

    @DeleteMapping(path ="/{userId}")
    public UserProfileResponse deleteUserProfile(@PathVariable("userId") Long userId) {
        return userProfileService.deleteUserProfileById(userId);
    }

    //Search result query filter endpoints

    @GetMapping(params = {"zip"})
    public UserProfileResponse getUserProfilesWithZip(@RequestParam(value = "zip") String zip) {
        return userSearchFilterService.filterUserProfilesByZip(zip);
    }

}
