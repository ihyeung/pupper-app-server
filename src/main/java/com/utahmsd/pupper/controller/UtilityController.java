package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.ZipCodeAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilityController {

    private final ZipCodeAPIClient zipCodeAPIClient;

    @Autowired
    public UtilityController(ZipCodeAPIClient zipCodeAPIClient) {
        this.zipCodeAPIClient = zipCodeAPIClient;
    }

    @GetMapping(path = "/zip", params = {"radius", "zipCode"})
    public void getZipcodesInRadius(@RequestParam("radius") String radius,
                                    @RequestParam("zipCode") String zipCode) {
        zipCodeAPIClient.getZipCodesInRadius(zipCode, radius);
    }

}
