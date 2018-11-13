package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperMessage;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MessageResponse extends BaseResponse {

    @JsonProperty("pupperMessages")
    private List<PupperMessage> messages;

    public static MessageResponse createMessageResponse(boolean success,
                                                        List<PupperMessage> messages,
                                                        HttpStatus code,
                                                        String description) {
        MessageResponse response = new MessageResponse();
        response.setSuccess(success);
        response.setMessages(messages);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public List<PupperMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PupperMessage> messages) {
        this.messages = messages;
    }
}
