package com.utahmsd.pupper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;

import javax.inject.Inject;
import java.io.IOException;

public abstract class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Inject
    private ResponseErrorHandler responseErrorHandler;

    public void logException(final Throwable th, ClientHttpResponse resp) throws IOException {
        if (th instanceof HttpServerErrorException) {
            LOGGER.error("Error was an Http server error Exception: {}", th.toString());
        } else if (th instanceof HttpClientErrorException) {
            LOGGER.error("Error was HTTP client Exception", th);
        } else if (th instanceof HttpStatusCodeException) {
            LOGGER.error("Error was HTTP status code exception", th);
        }
        responseErrorHandler.handleError(resp);
    }

}

