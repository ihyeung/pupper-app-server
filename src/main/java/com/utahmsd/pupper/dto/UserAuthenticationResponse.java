package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserAuthenticationResponse extends BaseResponse {

    @JsonProperty("users")
    private List<UserAccount> users;

    @JsonProperty("token")
    private String authToken;

    @JsonProperty("expiresIn")
    private long secondsToExpiration;

    public static UserAuthenticationResponse createUserAuthResponse(boolean success,
                                                                        List<UserAccount> users,
                                                                        HttpStatus code,
                                                                        String description) {
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setSuccess(success);
        response.setUsers(users);
        response.setStatusCode(code);
        response.setDescription(description);
        return response;
    }

    public List<UserAccount> getUsers() {
        return users;
    }

    public void setUsers(List<UserAccount> users) {
        this.users = users;
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