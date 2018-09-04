package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAuthenticationResponse extends ProfileResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("lastLogin")
    private Timestamp lastLogin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
//
//    @JsonProperty("jwt")
//    private String authToken;
//
//    @JsonProperty("tokenIssued")
//    private Timestamp tokenIssued;

//    public UserAuthenticationResponse fromUserAccount (UserAccount userAccount) {
//        UserAuthenticationResponse response = new UserAuthenticationResponse();
//        response.userId = userAccount.getUserId();
//        response.statusCode = 0;
//        response.validLogin = true;
//        response.accountCreateDate = userAccount.getAccountCreationDate();
//        response.authToken = userAccount.getComputedHash();
//        return response;
//    }

    public UserAuthenticationResponse invalidProfileResponse(Long id, String error) {
        UserAuthenticationResponse userAuthenticationResponse = new UserAuthenticationResponse();
        userAuthenticationResponse.setId(id);
//        userAuthenticationResponse.setLastLogin(null);
        userAuthenticationResponse.setStatus(error);
        userAuthenticationResponse.setSuccess(false);
        return userAuthenticationResponse;
    }

}