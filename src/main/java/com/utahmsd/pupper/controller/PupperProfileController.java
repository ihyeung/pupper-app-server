package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.service.PupperProfileService;
import com.utahmsd.pupper.service.filter.PupperSearchFilterService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;

//Controller class for viewing/modifying a pupper profile

@RestController
@Api(value = "PupperProfile Controller For Pupper Endpoints")
public class PupperProfileController {

    private final PupperProfileService pupperProfileService;
    private final PupperSearchFilterService pupperSearchFilterService;

    @Inject
    PupperProfileController(PupperProfileService pupperProfileService, PupperSearchFilterService filterService) {
        this.pupperProfileService = pupperProfileService;
        this.pupperSearchFilterService = filterService;
    }

    @RequestMapping(method= RequestMethod.POST)
    public PupperProfileResponse createPupperProfile(@RequestBody PupperProfileRequest request) {

        return pupperProfileService.createNewPupperProfile(request);
    }

    @RequestMapping(path="/pupper", method= RequestMethod.GET)
    public PupperProfileResponse getAllPupperProfiles() {

        return pupperProfileService.getAllPupperProfiles();
    }

    @RequestMapping(path ="/user/{userId}/pupper/", method= RequestMethod.GET)
    public PupperProfileResponse getAllPupperProfilesForUser(@PathVariable("userId") Long userId) {

        return pupperProfileService.findAllPupperProfilesByUserId(userId);
    }
    @RequestMapping(path ="/user/{userId}/pupper/{pupperId}", method= RequestMethod.GET)
    public PupperProfileResponse getPupperProfile(@PathVariable("pupperId") Long pupId, @PathVariable("userId") Long userId) {

        return pupperProfileService.findPupperProfile(pupId, userId);
    }

    @RequestMapping(path ="/user/{userId}/pupper/{pupperId}", method= RequestMethod.PUT)
    public PupperProfileResponse updatePupperProfile(@PathVariable("userId") Long userId,
                                                     @PathVariable("pupperId") Long pupId,
                                                     @RequestBody PupperProfileRequest request) {

        return pupperProfileService.updatePupperProfile(request);
    }

    @RequestMapping(path ="/user/{userId}/pupper/{pupperId}", method= RequestMethod.DELETE)
    public PupperProfileResponse deletePupperProfile(@PathVariable("userId") Long userId,
                                                     @PathVariable("pupperId") Long pupperId) {

        return pupperProfileService.deletePupperProfile(userId, pupperId);
    }

}