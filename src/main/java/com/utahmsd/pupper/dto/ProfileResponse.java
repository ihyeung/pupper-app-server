package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ProfileResponse {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("UserId")
    private Long userId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPupId() {
        return pupId;
    }

    public void setPupId(Long pupId) {
        this.pupId = pupId;
    }

    @JsonProperty("PupId")
    private Long pupId;
}
