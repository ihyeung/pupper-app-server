package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.PupperMessage;
import com.utahmsd.pupper.dto.MessageResponse;
import com.utahmsd.pupper.service.PupperMessageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Message Controller For Endpoints Relating to the App's Messaging functionality")
@RequestMapping("/message")
public class MessageController {

    private final PupperMessageService pupperMessageService;

    @Autowired
    public MessageController (PupperMessageService pupperMessageService) {
        this.pupperMessageService = pupperMessageService;
    }

    @GetMapping()
    public List<PupperMessage> getAllMessages() {
        return pupperMessageService.getAllMessages();
    }

    @GetMapping(params = {"limit"})
    public List<PupperMessage> getAllMessagesWithLimit(@RequestParam("limit") int messageLimit) {
        return pupperMessageService.getMessagesWithLimit(messageLimit);
    }

    /*
    Gets all messages ever sent or received by a given matchProfileId.
     */
    @GetMapping(path="/matchProfile/{matchProfileId}")
    public List<PupperMessage> getAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMessageService.getAllMessagesByMatchProfileId(matchProfileId);
    }

    /*
    Retrieves messages sent/received between matchProfileId1 and matchProfileId2.
     */
    @GetMapping(path="/matchProfile/{matchProfileId1}/matchProfile/{matchProfileId2}")
    public List<PupperMessage> getMessageHistory(@PathVariable("matchProfileId1") Long matchProfileId1,
                                             @PathVariable("matchProfileId2") Long matchProfileId2) {
        return pupperMessageService.getMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
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
    @PostMapping(path="/matchProfile/{fromMatchProfileId}/matchProfile/{toMatchProfileId}")
    public MessageResponse sendMessageToMatch(@PathVariable("fromMatchProfileId") Long senderId,
                                              @PathVariable("toMatchProfileId") Long receiverId,
                                              @RequestBody @Valid final PupperMessage pupperMessage) {

        return pupperMessageService.sendMessage(senderId, receiverId, pupperMessage);
    }

    /*
    Delete endpoint that removes all pupper_message messages between a given pair of matchProfiles.
    Use case: A matchProfile unmatches with another matchProfile.
     */
    @DeleteMapping(path="/matchProfile/{matchProfileId1}/matchProfile/{matchProfileId2}")
    public MessageResponse deleteAllMessagesBetweenMatchProfilesById(@PathVariable("matchProfileId1") Long matchProfileId1,
                                                                     @PathVariable("matchProfileId2") Long matchProfileId2) {
        return pupperMessageService.deleteMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /*
   Delete endpoint that removes all pupper_message messages sent/received by a given matchProfileId.
   Use case: A matchProfile or userProfile is deleted.
    */
    @DeleteMapping(path="/matchProfile/{matchProfileId}")
    public MessageResponse deleteAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {
        return pupperMessageService.deleteAllMessagesByMatchProfileId(matchProfileId);
    }
}
