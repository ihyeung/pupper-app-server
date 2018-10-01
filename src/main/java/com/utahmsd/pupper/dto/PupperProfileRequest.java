package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperProfile;

//PupperProfile profile request representing a json request for a given random matching profile displayed to user
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileRequest {

    @JsonProperty("requestingUserId")
    private Long requestingUserId;

    @JsonProperty("requestingPupperId")
    private Long requestingPupperId;

    @JsonProperty("pupper") //PupperProfile that was requested, corresponds to id field
    private PupperProfile pupperProfile;

    @JsonProperty("userId") //Id of the user that this pupperProfile belongs to
    private Long pupperUserId;

    public Long getRequestingUserId() {
        return requestingPupperId;
    }

    public void setRequestingUserId(Long requestingPupperId) {
        this.requestingPupperId = requestingPupperId;
    }

    public Long getRequestingPupperId() {
        return requestingPupperId;
    }

    public void setRequestingPupperId(Long requestingPupperId) {
        this.requestingPupperId = requestingPupperId;
    }

    public PupperProfile getPupperProfile() {
        return pupperProfile;
    }

    public void setPupperProfile(PupperProfile pupperProfile) {
        this.pupperProfile = pupperProfile;
    }

    public Long getPupperUserId() {
        return pupperUserId;
    }

    public void setPupperUserId(Long pupperUserId) {
        this.pupperUserId = pupperUserId;
    }
}
