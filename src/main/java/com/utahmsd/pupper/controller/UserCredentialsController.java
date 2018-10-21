package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserCredentialsResponse;
import com.utahmsd.pupper.service.UserCredentialsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

@RestController
@Api(value = "UserCredentials Controller For Testing UserCredential Table (Remove this later!)")
@RequestMapping("/")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    @Inject
    UserCredentialsController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @RequestMapping(path="credential", method= RequestMethod.GET)
    public UserCredentialsResponse getAllUserCredentials() {
        return userCredentialsService.getAllUserCredentials();
    }

    @RequestMapping(path="credential/{userCredentialsId}", method= RequestMethod.GET)
    public UserCredentialsResponse getUserCredentials(@PathVariable("userCredentialsId") Long userCredentialsId) {
        return userCredentialsService.findUserCredentials(userCredentialsId);
    }

    @RequestMapping(path="user/{userId}/credential}", method= RequestMethod.GET)
    public UserCredentialsResponse getUserCredentialsForUser(@PathVariable("userId") Long userProfileId) {
        return null;
    }
}
