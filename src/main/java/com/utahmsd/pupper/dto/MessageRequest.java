package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageRequest {

    @JsonProperty("id")
    private Long messageId; //For caching messaging history

    @JsonProperty("toPupId")
    private Long toPupperId; //Pupper sending message

    @JsonProperty("toUserId")
    private Long toUserId; //Owner of pupper sending message

    @JsonProperty("fromPupId")
    private Long fromPupperId; //Pupper receiving message

    @JsonProperty("fromUserId")
    private Long fromUserId; //Owner of pupper receiving message

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private DateTime timestamp;

}
