package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MatchProfileResponse extends BaseResponse {

    @JsonProperty("match_profile")
    private List<MatchProfile> matchProfileList;

    public static MatchProfileResponse createMatchProfileResponse(boolean success,
                                                                  List<MatchProfile> matchProfiles,
                                                                  HttpStatus code,
                                                                  String description) {
        MatchProfileResponse response = new MatchProfileResponse();
        response.setSuccess(success);
        response.setMatchProfileList(matchProfiles);
        response.setStatusCode(code);
        response.setDescription(description);
        return response;
    }

    public List<MatchProfile> getMatchProfileList() {
        return matchProfileList;
    }

    public void setMatchProfileList(List<MatchProfile> matchProfileList) {
        this.matchProfileList = matchProfileList;
    }
}
