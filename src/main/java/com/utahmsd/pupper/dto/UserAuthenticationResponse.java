package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAuthenticationResponse {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("accountLogin")
    private String accountLogin;

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("joinDate")
    private Date accountCreateDate;

    @JsonProperty("jwt")
    private String authToken;

    @JsonProperty("tokenIssued")
    private Timestamp tokenIssued;

    @JsonProperty("isValid")
    private boolean validLogin;

    public UserAuthenticationResponse fromUserAccount (UserAccount userAccount) {
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.userId = userAccount.getUserId();
        response.statusCode = 0;
        response.validLogin = true;
        response.accountCreateDate = userAccount.getAccountCreationDate();
        response.authToken = userAccount.getComputedHash();
        return response;
    }

}