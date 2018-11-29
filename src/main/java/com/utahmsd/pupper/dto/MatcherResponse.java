package com.utahmsd.pupper.dto;

import org.springframework.http.HttpStatus;

public class MatcherResponse extends BaseResponse {

    public static MatcherResponse createPupperMatcherResponse(boolean success, HttpStatus code, String description) {
        MatcherResponse response = new MatcherResponse();
        response.setSuccess(success);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }
}
