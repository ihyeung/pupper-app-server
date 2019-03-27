package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class UserAuthenticationResponse extends BaseResponse {

    @JsonProperty("userAccounts")
    private List<UserAccount> users;


    public static UserAuthenticationResponse createUserAuthResponse(boolean success, List<UserAccount> users, HttpStatus code,
                                                                        String description) {
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setSuccess(success);
        response.setUsers(users == null ? new ArrayList<>() : users);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code, String description) {
        List<UserAccount> userAccounts = (List<UserAccount>)(List<?>)entityList;
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setSuccess(success);
        response.setUsers(userAccounts == null ? new ArrayList<>() : userAccounts);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return (T) response;
    }


    public List<UserAccount> getUsers() {
        return users;
    }

    public void setUsers(List<UserAccount> users) {
        this.users = users;
    }
}