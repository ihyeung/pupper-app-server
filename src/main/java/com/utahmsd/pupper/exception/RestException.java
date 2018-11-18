package com.utahmsd.pupper.exception;

import com.utahmsd.pupper.dto.BaseResponse;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;

public class RestException extends WebApplicationException {

        private final BaseResponse errorResponse;

        private RestException(String message, HttpStatus status) {
            this(message, status, null);
        }

        public RestException(String message, HttpStatus status, Throwable cause) {
            super(message, cause);
            this.errorResponse = new BaseResponse();
            this.errorResponse.setSuccess(false);
            this.errorResponse.setStatus(status);
            this.errorResponse.setDescription(message);
            this.errorResponse.setResponseCode(status.value());
        }

        public BaseResponse getErrorResponse() {
            return this.errorResponse;
        }
}
