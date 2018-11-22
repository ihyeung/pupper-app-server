package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.MessageRepo;
import com.utahmsd.pupper.dao.entity.MatchResult;
import com.utahmsd.pupper.dao.entity.PupperMessage;
import com.utahmsd.pupper.dto.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.utahmsd.pupper.dto.MessageResponse.createMessageResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static java.util.Collections.emptyList;

@Named
@Singleton
public class PupperMessageService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PupperMessageService.class);

    private final String DEFAULT_SORT_ORDER = "timestamp";

    private final MessageRepo messageRepo;
    private final MatchResultRepo matchResultRepo;

    @Autowired
    PupperMessageService(MessageRepo messageRepo, MatchResultRepo matchResultRepo) {
        this.messageRepo = messageRepo;
        this.matchResultRepo = matchResultRepo;
    }

    public List<PupperMessage> getAllMessages() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER)); //Sorts messages from oldest to newest
        Page<PupperMessage> results = messageRepo.findAll(PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, sort));
        ArrayList<PupperMessage> pupperMessages = new ArrayList<>();
        results.forEach(each -> pupperMessages.add(hideSensitiveData(each)));

        return pupperMessages;
    }

    public List<PupperMessage> getMessagesWithLimit(int limit) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER)); //Sorts messages from oldest to newest
        Page<PupperMessage> results = messageRepo.findAll(PageRequest.of(DEFAULT_PAGE_NUM, limit, sort));
        int numResults = results.getNumberOfElements();
        LOGGER.info("Number of messages returned: {}", numResults);
        results.forEach(this::hideSensitiveData);

        return results.getContent();
    }

    public List<PupperMessage> getAllMessagesByMatchProfileId(Long matchProfileId) {
        Optional<List<PupperMessage>> messageList =
                messageRepo.findAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(matchProfileId, matchProfileId);
        if (!messageList.isPresent()) {
            LOGGER.info("No messages were found for matchProfileId={}", matchProfileId);
            return emptyList();
        }

        LOGGER.info("{} messages were found that were sent/received by matchProfileId={}", messageList.get().size(), matchProfileId);

        messageList.get().forEach(this::hideSensitiveData);

        return messageList.get();
    }

    public List<PupperMessage> getMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        Optional<List<PupperMessage>> messagesFrom1To2 =
                messageRepo.findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(matchProfileId1, matchProfileId2);
        Optional<List<PupperMessage>> messagesFrom2To1 =
                messageRepo.findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(matchProfileId2, matchProfileId1);

        if (!messagesFrom1To2.isPresent() && !messagesFrom2To1.isPresent()) {
            LOGGER.info("No messages were exchanged between matchProfileId={} and received by matchProfileId={}", matchProfileId1, matchProfileId2);
            return emptyList();
        }

        ArrayList<PupperMessage> messageHistory = new ArrayList<>();
        if (messagesFrom1To2.isPresent()) {
            LOGGER.info("{} messages were sent from matchProfileId={} and received by matchProfileId={}",
                    messagesFrom1To2.get().size(), matchProfileId1, matchProfileId2);
            messageHistory.addAll(messagesFrom1To2.get());
        }
        if (messagesFrom2To1.isPresent()) {
            LOGGER.info("{} messages were sent from matchProfileId={} and received by matchProfileId={}",
                    messagesFrom2To1.get().size(), matchProfileId2, matchProfileId1);
            messageHistory.addAll(messagesFrom2To1.get());
        }

        messageHistory.forEach(this::hideSensitiveData);

        LOGGER.info("{} messages were exchanged between matchProfileId={} and matchProfileId={}",
                messageHistory.size(), matchProfileId1, matchProfileId2);

        messageHistory.sort(Comparator.comparing(PupperMessage::getTimestamp)); //Sort messages by oldest to newest

        return messageHistory;
    }

    public MessageResponse sendMessage(Long senderId, Long receiverId, final PupperMessage pupperMessage) {
        if (!pupperMessage.getMatchProfileSender().getId().equals(senderId) ||
                !pupperMessage.getMatchProfileReceiver().getId().equals(receiverId)) {
            return createMessageResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        else if (!isValidMatchResult(senderId, receiverId)) {
            LOGGER.error("Error: message cannot be sent between the matchProfiles matchProfileId={} and matchProfileId={} " +
                    "because they are not a mutual match.", senderId, receiverId);

            return createMessageResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    "Error sending message: Not a valid matchResult.");
        }
        messageRepo.save(pupperMessage);

        return createMessageResponse(true, new ArrayList<>(Arrays.asList(hideSensitiveData(pupperMessage))),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    //TODO: Implement delete methods

    public MessageResponse deleteMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        return null;
    }

    public MessageResponse deleteAllMessagesByMatchProfileId(Long matchProfileId) {
        return null;
    }


    /**
     * Helper method that verifies that two match profiles attempting to exchange messages are a match.
     * @param matchProfileId1
     * @param matchProfileId2
     * @return
     */
    private boolean isValidMatchResult(Long matchProfileId1, Long matchProfileId2) {
        Optional<MatchResult> matchResult1 = matchResultRepo.findByMatchProfileOne_IdAndMatchProfileTwo_Id(matchProfileId1, matchProfileId2);
        Optional<MatchResult> matchResult2 = matchResultRepo.findByMatchProfileOne_IdAndMatchProfileTwo_Id(matchProfileId2, matchProfileId1);

        if (!matchResult1.isPresent() && !matchResult2.isPresent()) {
            LOGGER.error("No match result outcome exists between matchProfileId={} and matchProfileId={}", matchProfileId1, matchProfileId2);
            return false;
        }

        return matchResult1
                .map(m1 -> m1.isMatchForProfileOne() && m1.isMatchForProfileTwo())
                .orElseGet(() -> matchResult2.get().isMatchForProfileOne() && matchResult2.get().isMatchForProfileTwo());
    }

    private PupperMessage hideSensitiveData(PupperMessage pupperMessage) {
        pupperMessage.getMatchProfileSender().getUserProfile().getUserAccount().setPassword(null);
        pupperMessage.getMatchProfileReceiver().getUserProfile().getUserAccount().setPassword(null);

        return pupperMessage;
    }

}
