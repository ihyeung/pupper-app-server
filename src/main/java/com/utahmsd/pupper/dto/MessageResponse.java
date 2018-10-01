package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageResponse {

    @JsonProperty("id")
    private Long messageId;

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("statusCode")
    private HttpStatus statusCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("timestamp")
    private DateTime timestamp;

}
