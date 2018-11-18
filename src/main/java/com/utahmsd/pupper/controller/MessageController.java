package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.MessageRequest;
import com.utahmsd.pupper.dto.MessageResponse;
import com.utahmsd.pupper.service.PupperMessagingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Message Controller For Endpoints Relating to the App's Messaging functionality")
@RequestMapping("/message")
public class MessageController {

    private final PupperMessagingService pupperMessagingService;

    @Autowired
    public MessageController (PupperMessagingService pupperMessagingService) {
        this. pupperMessagingService = pupperMessagingService;
    }

    @GetMapping(path="/{matchProfileId}")
    public MessageResponse getMessages(@PathVariable("matchProfileId") Long matchProfileId) {

        return pupperMessagingService.getMessages(matchProfileId);
    }

    @PostMapping(path="/senderId/{senderMatchProfileId}/receiverId/{receiverMatchProfileId}")
    public MessageResponse sendMessageToMatch(@PathVariable("senderMatchProfileId") Long senderId,
                                       @PathVariable("receiverMatchProfileId") Long receiverId,
                                       @RequestBody final MessageRequest messageRequest) {

        return pupperMessagingService.sendMessage(senderId, receiverId, messageRequest);
    }

    @DeleteMapping(path="/senderId/{senderMatchProfileId}/receiverId/{receiverMatchProfileId}")
    public MessageResponse deleteAllMessagesByMatchProfileId(@PathVariable("senderMatchProfileId") Long senderId,
                                                                 @PathVariable("receiverMatchProfileId") Long receiverId) {
        return null;
    }

    @DeleteMapping(path="/{matchProfileId}")
    public MessageResponse deleteAllMessages(@PathVariable("matchProfileId") Long matchProfileId) {
        return null;
    }
}
