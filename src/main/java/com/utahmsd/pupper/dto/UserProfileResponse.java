package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.UserProfile;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileResponse extends ProfileResponse {

    @JsonProperty("userProfile")
    private UserProfile userProfile;


    //Constructors--need to clean these up later

    public UserProfileResponse() {
    }

    public UserProfileResponse(UserProfileRequest request){
        this.setId(request.getId());
        this.setUserProfile(request.getUserProfile());  //may be null if not doing an update to an existing profile
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
        response.setUserProfile(null);
        return response;
    }

    public static UserProfileResponse fromUserProfile (UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(userProfile.getId());
        response.setSuccess(true);
        response.setStatus("fromUserProfile success");
        response.setUserProfile(userProfile);
        return response;
    }


    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
