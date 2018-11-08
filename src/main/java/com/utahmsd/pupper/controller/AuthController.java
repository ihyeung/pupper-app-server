package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAccountService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.utahmsd.pupper.security.SecurityConstants.REGISTER_ENDPOINT;

@RestController
@Api(value = "UserAccount/Auth Controller For Testing UserAccount")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

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
        return userAccountService.findUserCredentialsByAccountId(accountId);
    }

    @PostMapping(path = REGISTER_ENDPOINT)
    public UserAuthenticationResponse registerUser(@RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.createUserCredentials(userAccount);
    }

    @PutMapping(path = "/account/{accountId}")
    public UserAuthenticationResponse updateCredentialsById(@PathVariable("accountId") Long accountId,
                                                            @RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.updateUserCredentialsById(accountId, userAccount);
    }

    @DeleteMapping(path = "/account/{accountId}")
    public UserAuthenticationResponse deleteCredentialsById(@PathVariable("accountId") Long accountId) {
        return userAccountService.deleteUserCredentialsById(accountId);
    }

//    @PostMapping(path = "auth/login")
//    public UserCredentialsResponse authenticateUser(@RequestBody UserCredentialsRequest request) {
//        return userCredentialsService.authenticateUser(request);
//    }

    @GetMapping(path="/user/{userId}/account")
    public UserAuthenticationResponse getCredentialsByUserId(@PathVariable("userId") Long userProfileId) {
        return userAccountService.findUserCredentialsByUserProfileId(userProfileId);
    }
}
