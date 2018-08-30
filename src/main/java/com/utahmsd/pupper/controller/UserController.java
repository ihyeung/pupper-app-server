package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.*;
import com.utahmsd.pupper.service.UserProfileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

//Controller for user authentication, login, and user profile endpoints

@Path("/user")
@Api("UserProfile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController extends BaseController {

    @Inject
    UserProfileService userProfileService;

    @Path("/join")
    @POST
    public UserAuthenticationResponse createAccount(UserAuthenticationRequest request) {
        return userProfileService.createUser(request);
    }

    @Path("/login")
    @POST
    public UserAuthenticationResponse login(@Valid UserAuthenticationRequest request) {
        return userProfileService.authenticateUser(request);
    }

    @Path("/{id}")
    @GET
    public UserProfileResponse getUserProfile(@PathParam("id") int id) {
        return userProfileService.findUserProfile(id);
    }

    @Path("/{id}")
    @POST
    public UserProfileResponse updateUserProfile(@Valid UserProfileRequest request, @PathParam("id") int id) {
        return null;
    }

    @Path("/{id}")
    @DELETE
    public UserProfileResponse deleteUserProfile(@PathParam("id") int userid) {
        return null;
    }

}
