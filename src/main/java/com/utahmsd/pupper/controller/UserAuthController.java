package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import com.utahmsd.pupper.service.UserAuthenticationService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@Api(value = "UserAuth Controller For User Authentication Endpoints -- Remove this from the API later for security reasons")
@RequestMapping("/auth")
public class UserAuthController {

    private final UserAuthenticationService userAuthenticationService;

    @Inject
    UserAuthController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @RequestMapping(path = "/token", method = RequestMethod.GET)
    public UserAuthenticationResponse getAuthToken() {
        return userAuthenticationService.getAuthToken();
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public UserAuthenticationResponse createUserAccount(@RequestBody UserAuthenticationRequest request) {
//        return userAuthenticationService.createUserCredentials(request);
//    }
//
//    @RequestMapping(path = "/login", method = RequestMethod.POST)
//    public UserAuthenticationResponse loginUser(@RequestBody UserAuthenticationRequest request) {
//        return userAuthenticationService.authenticateUserCredentials(request);
//    }
//
//    @RequestMapping(path = "/{credentialsId}", method= RequestMethod.PUT)
//    public UserAuthenticationResponse updateUserAccount(@PathVariable("credentialsId") Long id,
//                                                        @RequestBody UserAuthenticationRequest request) {
//        return userAuthenticationService.updateUserCredentials(id, request);
//    }
//
//    @RequestMapping(path = "/{credentialsId}", method= RequestMethod.DELETE)
//    public UserAuthenticationResponse deleteUserAccount(@PathVariable("credentialsId") Long id) {
//        return userAuthenticationService.deleteUserCredentials(id);
//    }


}
