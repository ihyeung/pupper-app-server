package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.PupperMessage;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class MessageResponse extends BaseResponse {

    @JsonProperty("messages")
    public List<PupperMessage> messages;


    public MessageResponse() {}

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code,
                                                     String description) {
        List<PupperMessage> messages = (List<PupperMessage>)(List<?>)entityList;
        MessageResponse response = new MessageResponse();
        response.setSuccess(success);
        response.setMessages(messages == null ? new ArrayList<>() : messages);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);

        return (T) response;
    }

    public static MessageResponse createMessageResponse(boolean success,
                                                        List<PupperMessage> messages,
                                                        HttpStatus code,
                                                        String description) {
        MessageResponse response = new MessageResponse();
        response.setSuccess(success);
        response.setMessages(messages == null ? new ArrayList<>() : messages);
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
