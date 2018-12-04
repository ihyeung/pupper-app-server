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

    /**
     * Retrieves a specified number of messages, returned in order from newest to oldest.
     * @param messageLimit the number of messages to retrieve. If the limit specified exceeds a server-defined max,
     *                     the server-defined max is the number of messages that are returned.
     * @return the limit most recent messages
     */
    @GetMapping(params = {"limit"})
    public List<PupperMessage> getAllMessagesWithLimit(@RequestParam("limit") int messageLimit) {
        return messageService.getMessagesWithLimit(messageLimit);
    }

    /**
     * Retrieves all messages ever sent or received by a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @GetMapping(params = {"matchProfileId"})
    public List<PupperMessage> getMessagesByMatchProfileId(@RequestParam("matchProfileId") Long matchProfileId) {
        return messageService.getAllMessagesByMatchProfileId(matchProfileId);
    }

    /**
     * Retrieves all messages exchanged between two matchProfileIds.
     * Messages are returned in ascending order by timestamp (i.e., oldest to newest).
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @GetMapping(params = {"matchProfileId1", "matchProfileId2"})
    public List<PupperMessage> getMessageHistory(@RequestParam("matchProfileId1") Long matchProfileId1,
                                                 @RequestParam("matchProfileId2") Long matchProfileId2) {
        return messageService.getCompleteMessageHistoryBetweenMatchProfiles(matchProfileId1, matchProfileId2);
    }

    /**
     * Retrieves all conversations between a given matchProfileId and its matches.
     * @param matchProfileId1
     * @return
     */
    @GetMapping(params = {"matchProfileId1"})
    public List<List<PupperMessage>> getMessageHistoriesForAllMatches(@RequestParam("matchProfileId1") Long matchProfileId1) {
        return messageService.getMessageHistoriesForAllMatches(matchProfileId1);
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

    /**
     * Retrieves recent message history (i.e., the 10 most recent messages exchanged) between two matchProfileIds.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @GetMapping(path="/recent", params = {"matchProfileId1", "matchProfileId2"})
    public List<PupperMessage> getRecentMessageHistory(@RequestParam("matchProfileId1") Long matchProfileId1,
                                                       @RequestParam("matchProfileId2") Long matchProfileId2) {
        return messageService.getRecentMessageHistoryBetweenMatchProfiles(matchProfileId1, matchProfileId2);
    }

    /**
     * Insert endpoint used to send a message from a given matchProfileId to another matchProfileId.
     * @param senderId
     * @param receiverId
     * @param pupperMessage
     * @return
     */
    @PostMapping(params = {"sendFrom", "sendTo"})
    public MessageResponse sendMessageToMatch(@RequestParam("sendFrom") Long senderId,
                                              @RequestParam("sendTo") Long receiverId,
                                              @RequestBody @Valid PupperMessage pupperMessage) {

        return messageService.sendMessage(senderId, receiverId, pupperMessage);
    }

    /**
     * Delete endpoint that removes all pupper_message messages between a given pair of matchProfiles.
     * Use case: A matchProfile elects to unmatch with another matchProfile.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    @DeleteMapping(params = {"matchProfileId1", "matchProfileId2"})
    public MessageResponse deleteAllMessagesBetweenMatchProfilesById(@RequestParam("matchProfileId1") Long matchProfileId1,
                                                                     @RequestParam("matchProfileId2") Long matchProfileId2) {
        return messageService.deleteMessageHistoryByMatchProfileIds(matchProfileId1, matchProfileId2);
    }

    /**
     * Delete endpoint that removes all pupper_message messages ever sent/received by a given matchProfileId.
     * Use case: A matchProfile is deleted, or a user deletes their account.
     * @param matchProfileId - matchProfileId to delete messages for
     * @return
     */
    @DeleteMapping(path="/matchProfile/{matchProfileId}")
    public MessageResponse deleteAllMessagesByMatchProfileId(@PathVariable("matchProfileId") Long matchProfileId) {
        return messageService.deleteAllMessagesByMatchProfileId(matchProfileId);
    }
}
