package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class PupperProfileResponse extends BaseResponse {

    @JsonProperty("pupperProfiles")
    private List<PupperProfile> pupperProfileList;


    public PupperProfileResponse() { }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code,
                                                     String description) {
        List<PupperProfile> pupperProfiles = (List<PupperProfile>)(List<?>)entityList;
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(success);
        response.setPupperProfileList(pupperProfiles == null ? new ArrayList<>() : pupperProfiles);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return (T) response;
    }

    public static PupperProfileResponse createPupperProfileResponse(boolean success,
                                                                    List<PupperProfile> puppers,
                                                                    HttpStatus code,
                                                                    String description) {
        PupperProfileResponse response = new PupperProfileResponse();
        response.setSuccess(success);
        response.setPupperProfileList(puppers == null ? new ArrayList<>() : puppers);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }


    public List<PupperProfile> getPupperProfileList() {
        return pupperProfileList;
    }

    public void setPupperProfileList(List<PupperProfile> pupperProfile) {
        this.pupperProfileList = pupperProfile;
    }
}
