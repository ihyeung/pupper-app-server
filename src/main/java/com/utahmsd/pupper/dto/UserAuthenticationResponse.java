package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAuthenticationResponse {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("jwt")
    private String authToken;

    @JsonProperty("tokenIssued")
    private Timestamp tokenIssued;

    @JsonProperty("isValid")
    private boolean tokenValid;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Timestamp getTokenIssued() {
        return tokenIssued;
    }

    public void setTokenIssued(Timestamp tokenIssued) {
        this.tokenIssued = tokenIssued;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }



}