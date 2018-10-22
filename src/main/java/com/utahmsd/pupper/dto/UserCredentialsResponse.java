package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UserCredentialsResponse extends BaseResponse {

    @JsonProperty("userList")
    private List<User> userList;

    public UserCredentialsResponse() {
    }

    public static UserCredentialsResponse createUserCredentialsResponse(boolean success,
                                                                        List<User> users,
                                                                        HttpStatus code,
                                                                        String description) {
        UserCredentialsResponse response = new UserCredentialsResponse();
        response.setSuccess(success);
        response.setUserList(users);
        response.setStatusCode(code);
        response.setDescription(description);
        return response;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
