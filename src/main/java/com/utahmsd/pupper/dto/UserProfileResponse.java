package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.UserProfile;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileResponse extends ProfileResponse {

    @JsonProperty("userProfile")
    private List<UserProfile> userProfile;


    //Constructors--need to clean these up later

    public UserProfileResponse() {
        userProfile = new ArrayList<>();
    }

    public UserProfileResponse(UserProfileRequest request){
        this.setId(request.getId());
//        this.setUserProfile(request.getUserProfile());  //may be null if not doing an update to an existing profile
        userProfile = new ArrayList<>();
        userProfile.add(request.getUserProfile());
    }

//    @Override
//    public UserProfileResponse responseErrorHandler (Long id, Exception ex, ProfileRequest request) throws Exception {
//        ProfileResponse response = super.responseErrorHandler(id,ex,request);
//        UserProfileResponse userProfileResponse = (UserProfileResponse) response;
//        userProfileResponse.setUserProfile(((UserProfileRequest)request).getUserProfile());
//        return userProfileResponse;
//    }

    //Static methods

    public static UserProfileResponse fromUserProfileRequest (UserProfileRequest request) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(request.getId());
        response.setSuccess(true);
        response.setStatus("fromUserProfileRequest success");
        return response;
    }

    public static UserProfileResponse fromUserProfile (UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(userProfile.getId());
        response.setSuccess(true);
        response.setStatus("fromUserProfile success");
        response.userProfile.add(userProfile);
        return response;
    }

    public List<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(List<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }
}
