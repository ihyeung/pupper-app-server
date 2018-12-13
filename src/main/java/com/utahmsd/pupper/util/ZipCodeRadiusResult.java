package com.utahmsd.pupper.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZipCodeRadiusResult {

    @JsonProperty("zip_code")
    private String zipcode;

    @JsonProperty("distance")
    private double distance;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    public ZipCodeRadiusResult(){}

    public ZipCodeRadiusResult(String zip, double distance, String city, String state) {
        this.zipcode = zip;
        this.distance = distance;
        this.city = city;
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
