package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.*;
import com.utahmsd.pupper.service.UserProfileService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

//Controller for user authentication, login, and user profile endpoints
@RestController
@Api(value = "UserProfile Controller For UserProfile Endpoints")
@RequestMapping("/user")
public class UserController {

    private final UserProfileService userProfileService;

    @Inject
    UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @RequestMapping(path ="/{id}", method= RequestMethod.GET)
    public UserProfileResponse getUserProfile(@PathVariable("id") Long id) {
        return userProfileService.findUserProfile(id);
    }

    @RequestMapping(path ="/{id}", method= RequestMethod.POST)
    public UserProfileResponse updateUserProfile(@PathVariable("id") Long id, @RequestBody UserProfileRequest request) {
        return userProfileService.updateProfile(request);
    }

    @RequestMapping(path ="/{id}", method= RequestMethod.DELETE)
    public UserProfileResponse deleteUserProfile(@PathVariable("id") Long id) {
        return userProfileService.deleteProfile(id);
    }

}
