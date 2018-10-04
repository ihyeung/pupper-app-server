package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileResponse extends ProfileResponse {

    //inherited ProfileResponse id field corresponds to pupperId

    @JsonProperty("pupperProfiles")
    private List<PupperProfile> pupperProfiles;

    public PupperProfileResponse() {
    }

    public static PupperProfileResponse createPupperProfileResponse(
            boolean success, List<PupperProfile> puppers, HttpStatus code) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(success);
        response.setPupperProfiles(puppers);
        response.setStatusCode(code);
        return response;
    }

    //Static methods

    public static PupperProfileResponse fromPupperProfileRequest(PupperProfileRequest request) {
        PupperProfileResponse response = new PupperProfileResponse();
        if (request.getPupperProfile() == null) {
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.NOT_FOUND);
            response.setDescription("fromPupperProfileRequest failure: request contains null pupper profile.");
            return response;
        }
        List<PupperProfile> data = new ArrayList<>();
        data.add(request.getPupperProfile());
        response.setPupperProfiles(data);
        response.setSuccess(true);
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public static PupperProfileResponse fromPupperProfile(PupperProfile profile) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(true);
        List<PupperProfile> data = new ArrayList<>();
        data.add(profile);
        response.setPupperProfiles(data);
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public List<PupperProfile> getPupperProfiles() {
        return pupperProfiles;
    }

    public void setPupperProfiles(List<PupperProfile> pupperProfile) {
        this.pupperProfiles = pupperProfile;
    }
}
