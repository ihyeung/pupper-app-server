package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.PupperMessage;
import com.utahmsd.pupper.dto.MessageResponse;
import com.utahmsd.pupper.service.MessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Message Controller For Endpoints Relating to the App's Messaging functionality")
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController (MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping()
    public List<PupperMessage> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping(params = {"limit"})
    public List<PupperMessage> getAllMessagesWithLimit(@RequestParam("limit") int messageLimit) {
        return messageService.getMessagesWithLimit(messageLimit);
    }

    /*
    Gets all messages ever sent or received by a given matchProfileId.
     */
    @GetMapping(path="/matchProfile/{matchProfileId}")
    public List<PupperMessage> getAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {
        return messageService.getAllMessagesByMatchProfileId(matchProfileId);
    }

    /*
    Retrieves messages sent/received between matchProfileId1 and matchProfileId2.
     */
    @GetMapping(path="/matchProfile/{matchProfileId1}/matchProfile/{matchProfileId2}")
    public List<PupperMessage> getMessageHistory(@PathVariable("matchProfileId1") Long matchProfileId1,
                                             @PathVariable("matchProfileId2") Long matchProfileId2) {
        return messageService.getMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /*
    Helper method to test that isValidMatchResult method is working.
     */
//    @GetMapping(path="/matchProfile/{matchProfileId1}/matchProfile/{matchProfileId2}/match")
//    public void checkForMatchResult(@PathVariable("matchProfileId1") Long matchProfileId1,
//                                                 @PathVariable("matchProfileId2") Long matchProfileId2) {
//        if (!pupperMessageService.isValidMatchResult(matchProfileId1, matchProfileId2)) {
//            System.out.println("not a valid match result");
//        } else {
//            System.out.println("This is a valid match result");
//
//        }
//    }

    /*
    Creates pupper_message record containing a message sent from fromMatchProfileId to toMatchProfileId.
     */
    @PostMapping(path="/matchProfile/{fromMatchProfileId}", params = {"sendTo"})
    public MessageResponse sendMessageToMatch(@PathVariable("fromMatchProfileId") Long senderId,
                                              @RequestParam("sendTo") Long receiverId,
                                              @RequestBody @Valid final PupperMessage pupperMessage) {

        return messageService.sendMessage(senderId, receiverId, pupperMessage);
    }

    /*
    Delete endpoint that removes all pupper_message messages between a given pair of matchProfiles.
    Use case: A matchProfile unmatches with another matchProfile.
     */
    @DeleteMapping(path="/matchProfile/{matchProfileId1}", params = {"messageFor"})
    public MessageResponse deleteAllMessagesBetweenMatchProfilesById(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                                     @RequestParam("messageFor") Long matchProfileId2) {
        return messageService.deleteMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /*
   Delete endpoint that removes all pupper_message messages sent/received by a given matchProfileId.
   Use case: A matchProfile or userProfile is deleted.
    */
    @DeleteMapping(path="/matchProfile/{matchProfileId}")
    public MessageResponse deleteAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {
        return messageService.deleteAllMessagesByMatchProfileId(matchProfileId);
    }
}
