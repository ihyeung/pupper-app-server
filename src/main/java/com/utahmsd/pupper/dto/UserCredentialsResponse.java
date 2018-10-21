package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserCredentials;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UserCredentialsResponse extends BaseResponse {

    @JsonProperty("userCredentialsList")
    private List<UserCredentials> userCredentialsList;

    public UserCredentialsResponse() {
    }

    public static UserCredentialsResponse createUserCredentialsResponse(boolean success,
                                                                        List<UserCredentials> users,
                                                                        HttpStatus code,
                                                                        String description) {
        UserCredentialsResponse response = new UserCredentialsResponse();
        response.setSuccess(success);
        response.setUserCredentialsList(users);
        response.setStatusCode(code);
        response.setDescription(description);
        return response;
    }

    public List<UserCredentials> getUserCredentialsList() {
        return userCredentialsList;
    }

    public void setUserCredentialsList(List<UserCredentials> userCredentialsList) {
        this.userCredentialsList = userCredentialsList;
    }
}
