package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;

import java.util.List;

public class MatchProfileResponse extends ProfileResponse {

    @JsonProperty("match_profile")
    private MatchProfile matchProfile;

    @JsonProperty("puppers")
    private List<PupperProfile> pupperProfileList;

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }

    public List<PupperProfile> getPupperProfileList() {
        return pupperProfileList;
    }

    public void setPupperProfileList(List<PupperProfile> pupperProfileList) {
        this.pupperProfileList = pupperProfileList;
    }
}
