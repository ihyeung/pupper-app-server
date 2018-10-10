package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageUploadRequest {

    @NotNull
    @JsonProperty("match_profile")
    private MatchProfile matchProfile;

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }
}
