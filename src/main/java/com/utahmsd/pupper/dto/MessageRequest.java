package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageRequest {

    @JsonProperty("senderId")
    private Long senderMatchProfileId;

    @JsonProperty("receiverId")
    private Long receiverMatchProfileId;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a")
    private String timestamp;

    @JsonProperty("messageContents")
    private String messageContents;

    public Long getSenderMatchProfileId() {
        return senderMatchProfileId;
    }

    public void setSenderMatchProfileId(Long senderMatchProfileId) {
        this.senderMatchProfileId = senderMatchProfileId;
    }

    public Long getReceiverMatchProfileId() {
        return receiverMatchProfileId;
    }

    public void setReceiverMatchProfileId(Long receiverMatchProfileId) {
        this.receiverMatchProfileId = receiverMatchProfileId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String message) {
        this.messageContents = message;
    }
}