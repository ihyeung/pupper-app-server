package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.ZipCodeAPIClient;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.service.filter.PupperProfileFilterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "Controller For Miscellaneous/Utility Endpoints")
public class UtilityController {

    private final ZipCodeAPIClient zipCodeAPIClient;
    private final PupperProfileFilterService pupperProfileFilterService;

    @Autowired
    public UtilityController(ZipCodeAPIClient zipCodeAPIClient, PupperProfileFilterService pupperProfileFilterService) {
        this.zipCodeAPIClient = zipCodeAPIClient;
        this.pupperProfileFilterService = pupperProfileFilterService;
    }

    @GetMapping(path = "/zip", params = {"radius", "zipCode"})
    public List<String> getZipcodesInRadius(@RequestParam("radius") String radius,
                                            @RequestParam("zipCode") String zipCode) {

        try {
            return zipCodeAPIClient.getZipCodesInRadius(zipCode, radius);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  Breed endpoints
     */

    @GetMapping(path ="/breed")
    public List<Breed> getAllBreeds() {
        return pupperProfileFilterService.getBreeds();
    }

    @GetMapping(path ="/breed", params = {"name"})
    public Breed getBreedByName(@RequestParam("name") String breedName) {
        return pupperProfileFilterService.getBreedFilterByName(breedName);
    }

}
