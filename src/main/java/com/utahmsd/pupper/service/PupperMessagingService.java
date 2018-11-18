package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MessageRepo;
import com.utahmsd.pupper.dto.MessageRequest;
import com.utahmsd.pupper.dto.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class PupperMessagingService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PupperMessagingService.class);

    private final MessageRepo messageRepo;

    @Autowired
    PupperMessagingService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public MessageResponse getMessages(Long matchProfileId) {
        return null;
    }

    public MessageResponse sendMessage(Long senderId, Long receiverId, final MessageRequest messageRequest) {
        return null;
    }

    public MessageResponse deleteMessageHistoryByMatchProfileId(Long senderId, Long receiverId) {
        return null;
    }

    public MessageResponse deleteAllMessagesByMatchProfileId(Long matchProfileId) {
        return null;
    }

}
