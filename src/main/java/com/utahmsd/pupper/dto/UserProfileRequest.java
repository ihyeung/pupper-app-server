package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileRequest extends ProfileRequest {

    @JsonProperty("IsNewUser")
    private boolean isNewUser;

    @JsonProperty("User")
    private User user;
}
