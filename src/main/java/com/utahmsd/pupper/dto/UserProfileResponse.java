package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UserProfileResponse extends BaseResponse {

    @JsonProperty("userProfiles")
    private List<UserProfile> userProfileList;

    public UserProfileResponse() { }

    public static UserProfileResponse createUserProfileResponse(boolean success,
                                                                List<UserProfile> users,
                                                                HttpStatus code,
                                                                String description) {

        UserProfileResponse response = new UserProfileResponse();
        response.setSuccess(success);
        response.setUserProfileList(users);
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
