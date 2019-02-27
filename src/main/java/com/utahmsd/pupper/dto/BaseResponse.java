package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class BaseResponse {

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("description")
    private String description;

    public abstract  <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList,
                                                               HttpStatus code, String description);

    public boolean isSuccess() { return success; }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
