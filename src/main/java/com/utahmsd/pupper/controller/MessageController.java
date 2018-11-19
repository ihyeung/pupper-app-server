package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dto.MessageRequest;
import com.utahmsd.pupper.dto.MessageResponse;
import com.utahmsd.pupper.service.PupperMessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Message Controller For Endpoints Relating to the App's Messaging functionality")
@RequestMapping("/message")
public class MessageController {

    private final PupperMessageService pupperMessageService;

    @Autowired
    public MessageController (PupperMessageService pupperMessageService) {
        this.pupperMessageService = pupperMessageService;
    }

    /*
    Gets all messages ever sent or received by a given matchProfileId.
     */
    @GetMapping(path="/{matchProfileId}")
    public MessageResponse getAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {

        return pupperMessageService.getAllMessagesByMatchProfileId(matchProfileId);
    }

    /*
    Retrieves messages sent/received between matchProfileId1 and matchProfileId2.
     */
    @GetMapping(path="/{matchProfileId1}/message/{matchProfileId2}")
    public MessageResponse getMessageHistory(@PathVariable("matchProfileId1") Long matchProfileId1,
                                             @PathVariable("matchProfileId2") Long matchProfileId2) {
        return pupperMessageService.getMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /*
    Creates pupper_message record containing a message sent from fromMatchProfileId to toMatchProfileId.
     */
    @PostMapping(path="/{fromMatchProfileId}/message/{toMatchProfileId}")
    public MessageResponse sendMessageToMatch(@PathVariable("fromMatchProfileId") Long senderId,
                                              @PathVariable("toMatchProfileId") Long receiverId,
                                              @RequestBody final MessageRequest messageRequest) {

        return pupperMessageService.sendMessage(senderId, receiverId, messageRequest);
    }

    /*
    Delete endpoint that removes all pupper_message messages between a given pair of matchProfiles.
    Use case: A matchProfile unmatches with another matchProfile.
     */
    @DeleteMapping(path="/{matchProfileId1}/message/{matchProfileId2}")
    public MessageResponse deleteAllMessagesByMatchProfileId(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                             @PathVariable("matchProfileId2") Long matchProfileId2) {
        return pupperMessageService.deleteMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /*
   Delete endpoint that removes all pupper_message messages sent/received by a given matchProfileId.
   Use case: A matchProfile or userProfile is deleted.
    */
    @DeleteMapping(path="/{matchProfileId}")
    public MessageResponse deleteAllMessages(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMessageService.deleteAllMessagesByMatchProfileId(matchProfileId);
    }
}
