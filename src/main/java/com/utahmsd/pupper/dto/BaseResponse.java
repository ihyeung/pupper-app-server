package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.exception.PupperRestExceptionHandler;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("statusCode")
    private HttpStatus statusCode;

    @JsonProperty("description")
    private String description;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BaseResponse(){}

    protected <T extends BaseResponse> T createResponse(boolean isSuccess, List<?> dataList, HttpStatus status, String description) {
        this.success = isSuccess;
        this.statusCode = status;
        this.description = description;

        return (T) this;
    }

}
