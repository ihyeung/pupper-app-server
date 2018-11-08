package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.service.PupperProfileService;
import com.utahmsd.pupper.service.filter.PupperSearchFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "PupperProfile Controller For PupperProfile Endpoints")
public class PupperProfileController {

    private final PupperProfileService pupperProfileService;
    private final PupperSearchFilterService pupperSearchFilterService;

    @Autowired
    PupperProfileController(PupperProfileService pupperProfileService, PupperSearchFilterService filterService) {
        this.pupperProfileService = pupperProfileService;
        this.pupperSearchFilterService = filterService;
    }

    @GetMapping(path="/pupper")
    public PupperProfileResponse getAllPupperProfiles() {

        return pupperProfileService.getAllPupperProfiles();
    }

    @GetMapping(path ="/breed")
    public PupperProfileResponse getAllPupperBreeds() {
        return pupperProfileService.getBreeds();
    }

    @GetMapping(path ="/user/{userId}/pupper/")
    public PupperProfileResponse getAllPupperProfilesForUser(@PathVariable("userId") Long userId) {
        return pupperProfileService.findAllPupperProfilesByUserId(userId);
    }

    @PostMapping(path="/user/{userId}/matchProfile/{matchProfileId}/pupper/")
    public PupperProfileResponse createPupperProfileForMatchProfile(@PathVariable("userId") Long userId,
                                                                    @PathVariable("matchProfileId") Long matchProfileId,
                                                                    @RequestBody @Valid final PupperProfile pupperProfile) {
        return pupperProfileService.createNewPupperProfileForMatchProfile(userId, matchProfileId, pupperProfile);
    }

    @GetMapping(path ="/user/{userId}/pupper/{pupperId}")
    public PupperProfileResponse findPupperProfileById(@PathVariable("userId") Long userId, @PathVariable("pupperId") Long pupId) {

        return pupperProfileService.findPupperProfileById(userId, pupId);
    }

    @PutMapping(path ="/user/{userId}/pupper/{pupperId}")
    public PupperProfileResponse updatePupperProfileById(@PathVariable("userId") Long userId,
                                                         @PathVariable("pupperId") Long pupId,
                                                         @RequestBody @Valid final PupperProfile pupperProfile) {
        return pupperProfileService.updatePupperProfile(userId, pupId, pupperProfile);
    }

    @DeleteMapping(path ="/user/{userId}/pupper/{pupperId}")
    public PupperProfileResponse deletePupperProfileById(@PathVariable("userId") Long userId,
                                                         @PathVariable("pupperId") Long pupperId) {

        return pupperProfileService.deletePupperProfileById(userId, pupperId);
    }

    @GetMapping(path ="/user/{userId}/matchProfile/{matchProfileId}/pupper")
    public PupperProfileResponse getAllPuppersInMatchProfile(@PathVariable("userId") Long userId,
                                                            @PathVariable("matchProfileId") Long matchProfileId) {
        return pupperProfileService.getAllPuppersByMatchProfileId(userId, matchProfileId);
    }

}