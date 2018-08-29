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

@Path("/user")
@Api("UserProfile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserProfileService userProfileService;

    @Path("/login")
    @POST
    public UserAuthenticationResponse login(@Valid UserAuthenticationRequest request) {
        return null;
    }

    @Path("/{userId}")
    @GET
    public UserProfileResponse getPupperProfile(@Valid UserProfileRequest request) {
        return null;
    }

    @Path("/{userId}")
    @POST
    public UserProfileResponse updatePupperProfile(@Valid UserProfileRequest request) {
        return null;
    }

    @Path("/{userId}")
    @DELETE
    public UserProfileResponse deletePupperProfile(@Valid UserProfileRequest request) {
        return null;
    }

}
