package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.dto.UserProfileRequest;
import com.utahmsd.pupper.service.UserProfileService;
import com.utahmsd.pupper.service.filter.UserSearchFilterService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@Api(value = "UserProfile Controller For User Endpoints")
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserSearchFilterService userSearchFilterService;

    //CRUD endpoints

    @Inject
    UserProfileController(UserProfileService userProfileService, UserSearchFilterService userSearchFilterService) {
        this.userProfileService = userProfileService;
        this.userSearchFilterService = userSearchFilterService;
    }

    @RequestMapping(method= RequestMethod.POST)
    public UserProfileResponse createUserProfile(@RequestBody UserProfileRequest request) {
        return userProfileService.createNewUserProfile(request);
    }

    @RequestMapping(path="/", method= RequestMethod.GET)
    public UserProfileResponse getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.GET)
    public UserProfileResponse getUserProfile(@PathVariable("userId") Long userId) {

        return userProfileService.findUserProfile(userId);
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.PUT)
    public UserProfileResponse updateUserProfile(@PathVariable("userId") Long userId, @RequestBody UserProfileRequest request) {
        return userProfileService.updateUserProfile(request);
    }

    @RequestMapping(path ="/{userId}", method= RequestMethod.DELETE)
    public UserProfileResponse deleteUserProfile(@PathVariable("userId") Long userId) {
        return userProfileService.deleteProfile(userId);
    }

    //Search result query filter endpoints

    @RequestMapping(params = {"zip"}, method= RequestMethod.GET)
    public UserProfileResponse getUserProfilesWithZip(@RequestParam(value = "zip") String zip) {
        return userSearchFilterService.filterUserProfilesByZip(zip);
    }

}
