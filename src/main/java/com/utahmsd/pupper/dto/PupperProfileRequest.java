package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

//Pupper profile request representing a json request for a given random matching profile displayed to user
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileRequest extends ProfileRequest {

    @JsonProperty("requestFrom")
    private Long requestingPupperId;

    public Long getRequestingUserId() {
        return requestingPupperId;
    }

    public void setRequestingUserId(Long requestingPupperId) {
        this.requestingPupperId = requestingPupperId;
    }
}
