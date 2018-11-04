package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "UserAccount Controller For Testing UserAccount")
public class AuthController {

    private final UserAccountService userAccountService;

    @Autowired
    AuthController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping(path="/account")
    public UserAuthenticationResponse getAllCredentials() {
        return userAccountService.getAllUserCredentials();
    }

    @GetMapping(path="/account/{accountId}")
    public UserAuthenticationResponse getCredentialsByAccountId(@PathVariable("accountId") Long accountId) {
        return userAccountService.findUserCredentialsById(accountId);
    }

    @PostMapping(path = "/register")
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
