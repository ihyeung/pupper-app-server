package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchPreference;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class MatchPreferenceResponse extends BaseResponse {

    @JsonProperty("matchPreferences")
    private List<MatchPreference> matchPreferenceList;

    public static MatchPreferenceResponse createResponse(boolean success,
                                                                  List<MatchPreference> matchPreferenceList,
                                                                  HttpStatus code,
                                                                  String description) {
        MatchPreferenceResponse response = new MatchPreferenceResponse();
        response.setSuccess(success);
        response.setMatchPreferenceList(matchPreferenceList == null ? new ArrayList<>() : matchPreferenceList);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return response;
    }

    public List<MatchPreference> getMatchPreferenceList() {
        return matchPreferenceList;
    }

    public void setMatchPreferenceList(List<MatchPreference> matchPreferenceList) {
        this.matchPreferenceList = matchPreferenceList;
    }
}
