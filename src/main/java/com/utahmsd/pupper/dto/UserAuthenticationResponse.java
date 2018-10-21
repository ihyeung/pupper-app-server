package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

public class UserAuthenticationResponse extends BaseResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("token")
    private String authToken;

    @JsonProperty("expiresIn")
    private long secondsToExpiration;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public long getSecondsToExpiration() {
        return secondsToExpiration;
    }

    public void setSecondsToExpiration(long secondsToExpiration) {
        this.secondsToExpiration = secondsToExpiration;
    }

}