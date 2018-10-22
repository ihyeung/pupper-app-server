package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserCredentialsRequest;
import com.utahmsd.pupper.dto.UserCredentialsResponse;
import com.utahmsd.pupper.service.UserCredentialsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@Api(value = "UserCredentials Controller For Testing UserCredential Table (Remove this later!)")
@RequestMapping("/")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    @Inject
    UserCredentialsController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @RequestMapping(path="credentials", method= RequestMethod.GET)
    public UserCredentialsResponse getAllUserCredentials() {
        return userCredentialsService.getAllUserCredentials();
    }

    @RequestMapping(path="credentials/{userCredentialsId}", method= RequestMethod.GET)
    public UserCredentialsResponse getUserCredentials(@PathVariable("userCredentialsId") Long userCredentialsId) {
        return userCredentialsService.findUserCredentials(userCredentialsId);
    }

    @RequestMapping(path = "credentials", method = RequestMethod.POST)
    public UserCredentialsResponse createUserCredentials(@RequestBody UserCredentialsRequest request) {
        return userCredentialsService.createUserCredentials(request);
    }

    @RequestMapping(path = "login", method = RequestMethod.POST)
    public UserCredentialsResponse authenticateUser(@RequestBody UserCredentialsRequest request) {
        return userCredentialsService.authenticateUser(request);
    }

    @RequestMapping(path="user/{userId}/credential}", method= RequestMethod.GET)
    public UserCredentialsResponse getUserCredentialsForUser(@PathVariable("userId") Long userProfileId) {
        return null;
    }
}
