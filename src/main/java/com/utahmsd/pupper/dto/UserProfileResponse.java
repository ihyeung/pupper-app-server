package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.UserProfile;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileResponse extends ProfileResponse {

    @JsonProperty("userProfile")
    private UserProfile userProfile;

    public UserProfileResponse() {
    }

    public UserProfileResponse(ProfileRequest request){
        super(request);
    }

    public static UserProfileResponse fromUserProfileRequest (UserProfileRequest request) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(request.getId());
        response.setSuccess(true);
        response.setStatus("success");
        response.setUserProfile(request.getUserProfile());
        return response;
    }

    public UserProfileResponse (Long id) {
        this.setId(id);
        this.setStatus("success");
        this.setSuccess(true);
        this.setUserProfile(null);
    }

    public UserProfileResponse(Long id, String error) {
        super(id, error);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
