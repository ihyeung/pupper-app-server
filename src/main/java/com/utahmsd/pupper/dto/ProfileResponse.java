package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.UserProfile;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class ProfileResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProfileResponse(){}

    public ProfileResponse(ProfileRequest request){
        id = request.getId();
        success = true;
        status = "success";
    }

    public ProfileResponse (Long id, String error){}
}
