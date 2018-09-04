package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.UserProfile;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserProfileRequest extends ProfileRequest {

    //Requests for a userProfile will always be made from a given UserProfile and not a PupperProfile


    @JsonProperty("requestingUser")
    private Long requestingUserId;

    @JsonProperty("requestingUserProfile") //UserProfile of user making request
    private UserProfile requestingUserProfile; //Probably will remove this once things are up and running

    @JsonProperty("userProfile") //UserProfile that was requested
    private UserProfile userProfile;

    public Long getRequestingUserId() {
        return requestingUserId;
    }

    public void setRequestingUserId(Long requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getRequestingUserProfile() {
        return requestingUserProfile;
    }

    public void setRequestingUserProfile(UserProfile requestingUserProfile) {
        this.requestingUserProfile = requestingUserProfile;
    }
}
