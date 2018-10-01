package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileResponse extends ProfileResponse {

    @JsonProperty("userProfiles")
    private List<UserProfile> userProfile;


    //Constructors--need to clean these up later

    public UserProfileResponse() {
    }

    public static UserProfileResponse createUserProfileResponse(boolean success, List<UserProfile> users, HttpStatus code) {
        UserProfileResponse response = new UserProfileResponse();
        response.setSuccess(success);
        response.setUserProfile(users);
        response.setStatusCode(code);
        return response;
    }

    public List<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(List<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }
}
