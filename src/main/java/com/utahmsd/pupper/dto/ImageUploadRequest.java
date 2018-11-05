package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageUploadRequest {

    @JsonProperty("match_profile")
    private MatchProfile matchProfile; //Match profile that the image being uploaded belongs to

    public ImageUploadRequest(){}

    public ImageUploadRequest(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }
}
