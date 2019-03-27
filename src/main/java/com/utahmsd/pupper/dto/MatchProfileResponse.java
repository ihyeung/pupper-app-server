package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class MatchProfileResponse extends BaseResponse {

    @JsonProperty("matchProfiles")
    private List<MatchProfile> matchProfileList;

    public static MatchProfileResponse createMatchProfileResponse(boolean success,
                                                                  List<MatchProfile> matchProfiles,
                                                                  HttpStatus code,
                                                                  String description) {
        MatchProfileResponse response = new MatchProfileResponse();
        response.setSuccess(success);
        response.setMatchProfileList(matchProfiles == null ? new ArrayList<>() : matchProfiles);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code, String description) {
        List<MatchProfile> matchProfiles = (List<MatchProfile>)(List<?>)entityList;
        MatchProfileResponse response = new MatchProfileResponse();
        response.setSuccess(success);
        response.setMatchProfileList(matchProfiles == null ? new ArrayList<>() : matchProfiles);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return (T) response;
    }

    public List<MatchProfile> getMatchProfileList() {
        return matchProfileList;
    }

    public void setMatchProfileList(List<MatchProfile> matchProfileList) {
        this.matchProfileList = matchProfileList;
    }
}
