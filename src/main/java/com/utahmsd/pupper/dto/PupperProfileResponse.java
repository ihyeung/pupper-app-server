package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import org.springframework.http.HttpStatus;

import java.util.List;

public class PupperProfileResponse extends BaseResponse {

    @JsonProperty("pupperProfiles")
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
