package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class UserProfileResponse extends BaseResponse {

    @JsonProperty("userProfiles")
    private List<UserProfile> userProfileList;

    public UserProfileResponse() { }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code,
                                                     String description) {
        List<UserProfile> userProfiles = (List<UserProfile>)(List<?>)entityList;
        UserProfileResponse response = new UserProfileResponse();
        response.setSuccess(success);
        response.setUserProfileList(userProfiles == null ? new ArrayList<>() : userProfiles);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return (T) response;
    }

    public static UserProfileResponse createUserProfileResponse(boolean success,
                                                                List<UserProfile> users,
                                                                HttpStatus code,
                                                                String description) {

        UserProfileResponse response = new UserProfileResponse();
        response.setSuccess(success);
        response.setUserProfileList(users == null ? new ArrayList<>() : users);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public List<UserProfile> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<UserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }
}
