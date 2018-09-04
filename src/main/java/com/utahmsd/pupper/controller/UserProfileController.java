package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserProfileRequest;
import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.service.UserProfileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

//Controller for user profile endpoints
@RestController
@Api(value = "UserProfile Controller For UserProfile Endpoints")
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Inject
    UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.GET)
    public UserProfileResponse getUserProfile(@PathVariable("userId") Long userId, UserProfileRequest request) {
           return userProfileService.findUserProfile(request);
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.POST)
    public UserProfileResponse updateUserProfile(@PathVariable("userId") Long userId, @RequestBody UserProfileRequest request) {
        return userProfileService.createOrUpdateProfile(request);
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.DELETE)
    public UserProfileResponse deleteUserProfile(@PathVariable("userId") Long userId, @RequestBody UserProfileRequest request) {
        return userProfileService.deleteProfile(request);
    }

}
