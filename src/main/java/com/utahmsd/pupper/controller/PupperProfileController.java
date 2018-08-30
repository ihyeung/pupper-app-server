package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.service.PupperProfileService;
import io.swagger.annotations.Api;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

//Controller class for viewing/modifying a pupper profile

@Path("/pupper")
@Api("PupperProfile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PupperProfileController {

    @Inject
    PupperProfileService pupperProfileService;


    @Path("/{pupperId}")
    @GET
    public PupperProfileResponse getPupperProfile(@PathParam("pupperId") int pupperId) {
        return pupperProfileService.findPupperProfile(pupperId);
    }

    @Path("/{pupperId}")
    @POST
    public PupperProfileResponse updatePupperProfile(@Valid PupperProfileRequest request) {
        return null;
    }

    @Path("/{pupperId}")
    @DELETE
    public PupperProfileResponse deletePupperProfile(@PathParam("pupperId") int pupperId) {
        return null;
    }


}
