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
public abstract class ProfileResponse {

    public static PupperRestExceptionHandler handler;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusCode")
    private HttpStatus statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("description")
    private String description;

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

//    public ProfileResponse(){}
//
//    public ProfileResponse(ProfileRequest request){
//        id = request.getId();
//        success = true;
//        statusCode = HttpStatus.OK;
//        status = "success";
//    }
//
//    public ProfileResponse responseErrorHandler (Long id, Exception ex, ProfileRequest request) throws Exception{
//        ResponseEntity<Object> errorResponse;
//        if (ex instanceof JsonParseException) {
//            errorResponse = handler.handleJsonParsingViolation((JsonParseException) ex, (WebRequest) request);
//        }
//        else if (ex instanceof ConstraintViolationException) {
//            errorResponse = handler.handleConstraintViolation((ConstraintViolationException) ex, (WebRequest) request);
//
//        } else {
//            errorResponse = handler.handleException(ex, (WebRequest) request);
//        }
//
//        this.id = id;
//        this.statusCode = errorResponse.getStatusCode();
//        this.success = errorResponse.getStatusCode().isError();
//        this.status = errorResponse.getStatusCode().getReasonPhrase();
//        this.message = ex.getMessage();
//        this.description = errorResponse.getBody().toString();
//        return this;
//    }
}
