package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.utahmsd.pupper.security.SecurityConstants.REGISTER_ENDPOINT;

@RestController
@Api(value = "UserAccount/Auth Controller For Testing UserAccount")
@RequestMapping("/account")
public class AuthController {

    private final UserAccountService userAccountService;

    @Autowired
    AuthController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping()
    public UserAuthenticationResponse getAllAccounts() {
        return userAccountService.getAllUserAccounts();
    }

    @GetMapping(path="/{accountId}")
    public UserAuthenticationResponse getAccountById(@PathVariable("accountId") Long accountId) {
        return userAccountService.findUserAccountById(accountId);
    }

    @GetMapping(params = {"email"})
    public UserAuthenticationResponse getAccountByEmail(@RequestParam("email") String email) {
        return userAccountService.findUserAccountByEmail(email);
    }

    @PostMapping(path = REGISTER_ENDPOINT)
    public UserAuthenticationResponse registerUser(@RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.createUserAccount(userAccount);
    }

    @PutMapping(path = "/{accountId}")
    public UserAuthenticationResponse updateAccountById(@PathVariable("accountId") Long accountId,
                                                        @RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.updateUserAccountById(accountId, userAccount);
    }

    @PutMapping(params = {"email"})
    public UserAuthenticationResponse updateAccountByEmail(@RequestParam("email") String email,
                                                           @RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.updateUserAccountByEmail(email, userAccount);
    }

    @DeleteMapping(path = "/{accountId}")
    public UserAuthenticationResponse deleteCredentialsById(@PathVariable("accountId") Long accountId) {
        return userAccountService.deleteUserAccountById(accountId);
    }

    @DeleteMapping(params = {"email"})
    public UserAuthenticationResponse deleteAccountByEmail(@RequestParam("email") String email) {
        return userAccountService.deleteUserAccountByEmail(email);
    }

    @GetMapping(params = {"userId"})
    public UserAuthenticationResponse getAccountByUserProfileId(@RequestParam("userId") Long userProfileId) {
        return userAccountService.getUserAccountByUserProfileId(userProfileId);
    }
}
