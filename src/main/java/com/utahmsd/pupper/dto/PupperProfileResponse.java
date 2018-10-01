package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileResponse extends ProfileResponse {

    //inherited ProfileResponse id field corresponds to pupperId

    @JsonProperty("pupperProfile")
    private PupperProfile pupperProfile;

    @JsonProperty("userId")
    private Long userId;  //UserId of the User that this PupperProfile
    // belongs to

    //Constructors--need to clean these up later

    public PupperProfileResponse() {}

    public PupperProfileResponse(PupperProfileRequest request) {
        this.setPupperProfile(request.getPupperProfile()); //may be null if not doing an update to an existing profile
    }

    //Static methods

    public static PupperProfileResponse fromPupperProfileRequest (PupperProfileRequest request) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setUserId(request.getPupperUserId());
        response.setSuccess(true);
        response.setPupperProfile(request.getPupperProfile());
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public static PupperProfileResponse fromPupperProfile (PupperProfile profile) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setUserId(profile.getUserId());
        response.setSuccess(true);
        response.setPupperProfile(profile);
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public PupperProfile getPupperProfile() {
        return pupperProfile;
    }

    public void setPupperProfile(PupperProfile pupperProfile) {
        this.pupperProfile = pupperProfile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
