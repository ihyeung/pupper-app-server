package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.service.PupperProfileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;

//Controller class for viewing/modifying a pupper profile

@RestController
@Api(value = "PupperProfile Controller For Pupper Profile Endpoints")
@RequestMapping("/puppers")
public class PupperProfileController {

    @Inject
    PupperProfileService pupperProfileService;


    @Path("/{pupId}")
    @GET
    public PupperProfileResponse getPupperProfile(@PathParam("pupId") Long pupId) {
        return pupperProfileService.findPupperProfile(pupId);
    }

    @Path("/{pupId}")
    @POST
    public PupperProfileResponse updatePupperProfile(@Valid PupperProfileRequest request) {
        return null;
    }

    @Path("/{pupId}")
    @DELETE
    public PupperProfileResponse deletePupperProfile(@PathParam("pupId") Long pupId) {
        return null;
    }


}
