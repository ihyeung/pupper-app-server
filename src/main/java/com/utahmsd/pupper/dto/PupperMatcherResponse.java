package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;

public class PupperMatcherResponse extends ProfileResponse {

    @JsonProperty("isMatch")
    private boolean isMatch; //Whether profile is a mutual match

    @JsonProperty("matchingProfile")
    private MatchProfile matchProfile; //The match profile of the user who is doing the swiping/clicking

    @JsonProperty("pupperMatch")
    private MatchProfile pupperMatch; //The match profile being shown to the user - will be null if not a two-way match

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }

    public MatchProfile getPupperMatch() {
        return pupperMatch;
    }

    public void setPupperMatch(MatchProfile pupperMatch) {
        this.pupperMatch = pupperMatch;
    }
}
