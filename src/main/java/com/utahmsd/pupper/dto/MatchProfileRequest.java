package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;

public class MatchProfileRequest {

    @JsonProperty("match_profile")
    private MatchProfile matchProfile;

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }
}
