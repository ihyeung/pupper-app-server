package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.http.HttpStatus;

import java.util.List;

public class PupperMatcherResponse extends BaseResponse {

    @JsonProperty("matchResults")
    private List<MatchResult> matchResults;

    public static PupperMatcherResponse createPupperMatcherResponse(boolean success, List<MatchResult> matchResults, HttpStatus code,
                                                                    String description) {
        PupperMatcherResponse response = new PupperMatcherResponse();
        response.setSuccess(success);
        response.setMatchResults(matchResults);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public List<MatchResult> getMatchResults() {
        return matchResults;
    }

    public void setMatchResults(List<MatchResult> matchResults) {
        this.matchResults = matchResults;
    }
}
