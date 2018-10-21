package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class PupperProfileResponse extends BaseResponse {

    //inherited BaseResponse id field corresponds to pupperId

    @JsonProperty("pupperProfileList")
    private List<PupperProfile> pupperProfileList;

    public PupperProfileResponse() {
    }

    public static PupperProfileResponse createPupperProfileResponse(boolean success,
                                                                    List<PupperProfile> puppers,
                                                                    HttpStatus code,
                                                                    String description) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(success);
        response.setPupperProfileList(puppers);
        response.setStatusCode(code);
        response.setDescription(description);
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
        response.setPupperProfileList(data);
        response.setSuccess(true);
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public static PupperProfileResponse fromPupperProfile(PupperProfile profile) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(true);
        List<PupperProfile> data = new ArrayList<>();
        data.add(profile);
        response.setPupperProfileList(data);
        response.setDescription("fromPupperProfileRequest success");

        return response;
    }

    public List<PupperProfile> getPupperProfileList() {
        return pupperProfileList;
    }

    public void setPupperProfileList(List<PupperProfile> pupperProfile) {
        this.pupperProfileList = pupperProfile;
    }
}
