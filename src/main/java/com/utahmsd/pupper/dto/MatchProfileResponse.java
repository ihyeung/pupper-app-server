package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MatchProfileResponse extends BaseResponse {

    @JsonProperty("match_profile")
    private List<MatchProfile> matchProfiles;

    public static MatchProfileResponse createMatchProfileResponse(
            boolean success, List<MatchProfile> matchProfiles, HttpStatus code) {
        MatchProfileResponse response = new MatchProfileResponse();
        response.setSuccess(success);
        response.setMatchProfiles(matchProfiles);
        response.setStatusCode(code);
        return response;
    }

    public List<MatchProfile> getMatchProfiles() {
        return matchProfiles;
    }

    public void setMatchProfiles(List<MatchProfile> matchProfiles) {
        this.matchProfiles = matchProfiles;
    }
}
