package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAccountService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@Api(value = "UserCredentials Controller For Testing UserCredential Table (Remove this later!)")
public class AuthController {

    private final UserAccountService userAccountService;

    @Inject
    AuthController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping(path="/auth")
    public List<UserAccount> getAllCredentials() {
        return userAccountService.getAllUserCredentials();
    }

    @GetMapping(path="/auth/{credentialsId}")
    public UserAccount getCredentials(@PathVariable("credentialsId") Long userCredentialsId) {
        return userAccountService.findUserCredentials(userCredentialsId);
    }

    @PostMapping(path = "auth/register")
    public UserAuthenticationResponse registerUser(@RequestBody UserAuthenticationRequest request) {
        return userAccountService.createUserCredentials(request);
    }

//    @PostMapping(path = "auth/login")
//    public UserCredentialsResponse authenticateUser(@RequestBody UserCredentialsRequest request) {
//        return userCredentialsService.authenticateUser(request);
//    }

//    @GetMapping(path="/user/{userId}/credentials}")
//    public UserCredentialsResponse getUserCredentialsForUser(@PathVariable("userId") Long userProfileId) {
//        return null;
//    }
}
