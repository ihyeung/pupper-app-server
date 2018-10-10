package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.exception.PupperRestExceptionHandler;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class BaseResponse {

    public static PupperRestExceptionHandler handler;

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("statusCode")
    private HttpStatus statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("description")
    private String description;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public BaseResponse(){}

}
