package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "User Account/Auth Controller")
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
        return userAccountService.findUserAccountByUserAccountId(accountId);
    }

    @PostMapping("/register")
    public UserAuthenticationResponse registerUser(@RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.registerNewUserAccount(userAccount);
    }

    @PutMapping(path = "/{accountId}")
    public UserAuthenticationResponse updateAccountById(@PathVariable("accountId") Long accountId,
                                                        @RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.updateUserAccountById(accountId, userAccount);
    }

    @DeleteMapping(path = "/{accountId}")
    public UserAuthenticationResponse deleteAccountById(@PathVariable("accountId") Long accountId) {
        return userAccountService.deleteUserAccountById(accountId);
    }

    @GetMapping(params = {"email"})
    public UserAuthenticationResponse getAccountByEmail(@RequestParam("email") String email) {
        return userAccountService.findUserAccountByEmail(email);
    }

    @PutMapping()
    public UserAuthenticationResponse updateAccountByEmail(@RequestParam("email") String email,
                                                           @RequestBody @Valid final UserAccount userAccount) {
        return userAccountService.updateUserAccountByEmail(email, userAccount);
    }

    @DeleteMapping()
    public UserAuthenticationResponse deleteAccountByEmail(@RequestParam("email") String email) {
        return userAccountService.deleteUserAccountByEmail(email);
    }

    @GetMapping(params = {"userId"})
    public UserAuthenticationResponse getAccountByUserProfileId(@RequestParam("userId") Long userProfileId) {
        return userAccountService.getUserAccountByUserProfileId(userProfileId);
    }
}
