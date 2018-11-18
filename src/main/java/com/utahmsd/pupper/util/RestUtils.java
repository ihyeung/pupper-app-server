package com.utahmsd.pupper.util;

import com.utahmsd.pupper.exception.RestException;

import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class RestUtils {
    public static RuntimeException mapException(final Throwable th) {
        if (th instanceof ValidationException) {
            return new RestException(
                    th.getMessage(), UNPROCESSABLE_ENTITY, th);
        }
        return new RestException(th.getMessage(), BAD_REQUEST, th);
    }
}