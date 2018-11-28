package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.BreedResponse;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.service.PupperProfileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "PupperProfile Controller For PupperProfile Endpoints")
public class PupperProfileController {

    private final PupperProfileService pupperProfileService;

    @Autowired
    PupperProfileController(PupperProfileService pupperProfileService) {
        this.pupperProfileService = pupperProfileService;
    }

    @GetMapping(path="/pupper")
    public PupperProfileResponse getAllPupperProfiles() {

        return pupperProfileService.getAllPupperProfiles();
    }

    @GetMapping(path="/pupper", params = {"sortBy", "limit"})
    public PupperProfileResponse getAllPupperProfiles(@RequestParam("sortBy") String sort,
                                                      @RequestParam("limit") int limit) {

        return pupperProfileService.getAllPupperProfiles(sort, limit);
    }

    @GetMapping(path ="/pupper/breed")
    public BreedResponse getPupperBreedList() {
        return pupperProfileService.getBreeds();
    }

    @GetMapping(path ="/pupper/breed/{breedId}")
    public PupperProfileResponse getPupperProfilesByBreedId(@PathVariable("breedId") Long breedId) {
        return pupperProfileService.getPupperProfilesByBreedId(breedId);
    }

    @GetMapping(path ="/pupper", params = {"breed"})
    public PupperProfileResponse getPupperProfilesByBreedId(@PathVariable("breed") String breed) {
        return pupperProfileService.getPupperProfilesByBreedName(breed);
    }

    @GetMapping(path ="/user/{userId}/pupper")
    public PupperProfileResponse getPupperProfilesByUserId(@PathVariable("userId") Long userId) {
        return pupperProfileService.findAllPupperProfilesByUserId(userId);
    }

    @GetMapping(path ="/pupper", params = {"userEmail"})
    public PupperProfileResponse getPupperProfilesByUserEmail(@RequestParam("userEmail") String userEmail) {
        return pupperProfileService.findAllPupperProfilesByUserEmail(userEmail);
    }

    @GetMapping(path ="/user/{userId}/matchProfile/{matchProfileId}/pupper")
    public PupperProfileResponse getPupperProfilesByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                                   @PathVariable("matchProfileId") Long matchProfileId) {
        return pupperProfileService.getPupperProfilesByMatchProfileId(userId, matchProfileId);
    }

    @PostMapping(path="/user/{userId}/matchProfile/{matchProfileId}/pupper")
    public PupperProfileResponse createPupperProfileByUserProfileIdAndMatchProfileId(@PathVariable("userId") Long userId,
                                                                    @PathVariable("matchProfileId") Long matchProfileId,
                                                                    @RequestBody @Valid final PupperProfile pupperProfile) {
        return pupperProfileService.createNewPupperProfileForMatchProfile(userId, matchProfileId, pupperProfile);
    }

    @GetMapping(path ="/user/{userId}/matchProfile/{matchProfileId}/pupper/{pupperId}")
    public PupperProfileResponse findPupperProfileById(@PathVariable("userId") Long userId,
                                                       @PathVariable("matchProfileId") Long matchProfileId,
                                                       @PathVariable("pupperId") Long pupId) {

        return pupperProfileService.findPupperProfileById(userId, matchProfileId, pupId);
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

}