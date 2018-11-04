package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("description")
    private String description;

    public static BaseResponse successResponse() {
        BaseResponse response = new BaseResponse();
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setDescription("Success");
        return response;
    }

    public static BaseResponse errorResponse(HttpStatus error, String description) {
        BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        response.setResponseCode(error.value());
        response.setStatus(error);
        response.setDescription(description);
        return response;

    }

    public boolean isSuccess() {
        return success;
    }

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
