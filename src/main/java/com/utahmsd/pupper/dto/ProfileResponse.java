package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ProfileResponse {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("UserId")
    private Long userId;

    @JsonProperty("PupId")
    private Long pupId;
}
