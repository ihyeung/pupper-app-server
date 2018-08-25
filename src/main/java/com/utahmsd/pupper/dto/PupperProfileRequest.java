package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PupperProfileRequest extends ProfileRequest {

    @JsonProperty("IsNew")
    private boolean isNew;

    @JsonProperty("Pupper")
    private Pupper pupper;
}
