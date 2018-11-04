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

    public static UserAuthenticationResponse createUserAuthResponse(boolean success, List<UserAccount> users, HttpStatus code,
                                                                        String description) {
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setSuccess(success);
        response.setUsers(users);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public List<UserAccount> getUsers() {
        return users;
    }

    public void setUsers(List<UserAccount> users) {
        this.users = users;
    }

}