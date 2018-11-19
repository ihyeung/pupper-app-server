package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MessageRepo;
import com.utahmsd.pupper.dto.MessageRequest;
import com.utahmsd.pupper.dto.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class PupperMessageService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PupperMessageService.class);

    private final MessageRepo messageRepo;

    @Autowired
    PupperMessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public MessageResponse getAllMessagesByMatchProfileId(Long matchProfileId) {
        return null;
    }

    public MessageResponse getMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        return null;
    }

    public MessageResponse sendMessage(Long senderId, Long receiverId, final MessageRequest messageRequest) {
        return null;
    }

    public MessageResponse deleteMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        return null;
    }

    public MessageResponse deleteAllMessagesByMatchProfileId(Long matchProfileId) {
        return null;
    }

}
