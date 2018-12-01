package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.MessageRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
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
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final String DEFAULT_SORT_ORDER = "timestamp";

    private final MessageRepo messageRepo;
    private final MatchResultRepo matchResultRepo;
    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    MessageService(MessageRepo messageRepo, MatchResultRepo matchResultRepo, MatchProfileRepo matchProfileRepo) {
        this.messageRepo = messageRepo;
        this.matchResultRepo = matchResultRepo;
        this.matchProfileRepo = matchProfileRepo;
    }

    public List<PupperMessage> getAllMessages() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER)); //Sorts messages from oldest to newest
        Page<PupperMessage> results = messageRepo.findAll(PageRequest.of(PAGE_NUM, PAGE_SIZE, sort));

        return results.getContent();
    }

    public List<PupperMessage> getMessagesWithLimit(int limit) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER)); //Sorts messages from oldest to newest
        Page<PupperMessage> results = messageRepo.findAll(PageRequest.of(PAGE_NUM, limit, sort));
        int numResults = results.getNumberOfElements();
        LOGGER.info("Number of messages returned: {}", numResults);

        return results.getContent();
    }

    public List<PupperMessage> getAllMessagesByMatchProfileId(Long matchProfileId) {
        Optional<List<PupperMessage>> messageList =
                messageRepo.findAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(matchProfileId, matchProfileId);
        if (!messageList.isPresent()) {
            LOGGER.error("No messages were found for matchProfileId={}", matchProfileId);

            return emptyList();
        }

        LOGGER.info("{} messages were found that were sent/received by matchProfileId={}", messageList.get().size(), matchProfileId);

        return messageList.get();
    }

    public List<PupperMessage> getMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        Optional<List<PupperMessage>> messagesFrom1To2 =
                messageRepo.findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(matchProfileId1, matchProfileId2);
        Optional<List<PupperMessage>> messagesFrom2To1 =
                messageRepo.findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(matchProfileId2, matchProfileId1);

        if (!messagesFrom1To2.isPresent() && !messagesFrom2To1.isPresent()) {
            LOGGER.error("No messages were exchanged between matchProfileId={} and received by matchProfileId={}", matchProfileId1, matchProfileId2);

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
        PupperMessage insertedMessage = messageRepo.save(pupperMessage);

        return createMessageResponse(true, Arrays.asList(insertedMessage), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MessageResponse deleteMessageHistoryByMatchProfileIds(Long matchProfileId1, Long matchProfileId2) {
        Optional<MatchProfile> matchProfile1 = matchProfileRepo.findById(matchProfileId1);
        Optional<MatchProfile> matchProfile2 = matchProfileRepo.findById(matchProfileId2);
        if (!matchProfile1.isPresent() || !matchProfile2.isPresent()) {
            return createMessageResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        messageRepo.deleteMessageHistoryBetweenMatchProfiles(matchProfile1.get(), matchProfile2.get());

        return createMessageResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    /**
     * Method that queries pupper_message for all records where either from_match_profile_id_fk or to_match_profile_id_fk
     * matches the matchProfileId supplied.
     * @param matchProfileId
     * @return
     */
    public MessageResponse deleteAllMessagesByMatchProfileId(Long matchProfileId) {
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);

        matchProfile.ifPresent(messageRepo::deleteAllMessagesForMatchProfile);

        return matchProfile.isPresent() ?
                createMessageResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION):
                createMessageResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }


    /**
     * Helper method that verifies that two match profiles attempting to exchange messages are a match.
     * @param matchProfileId1 match profile #1
     * @param matchProfileId2 match profile #2
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
}
