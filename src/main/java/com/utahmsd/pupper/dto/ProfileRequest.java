package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ProfileRequest {

    @JsonProperty("PupId")
    private Long pupId;

    public Long getPupId() {
        return pupId;
    }

    public void setPupId(Long pupId) {
        this.pupId = pupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("UserId")
    private Long userId;

}
