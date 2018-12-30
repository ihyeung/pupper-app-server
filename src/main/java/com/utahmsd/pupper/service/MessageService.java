package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MessageRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperMessage;
import com.utahmsd.pupper.dto.MessageResponse;
import com.utahmsd.pupper.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.MessageResponse.createMessageResponse;
import static com.utahmsd.pupper.util.Constants.*;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final String DEFAULT_SORT_ORDER = "timestamp";

    private final MessageRepo messageRepo;
    private final MatchProfileRepo matchProfileRepo;
    private final MatchProfileService matchProfileService;
    private final MatcherService matcherService;

    @Autowired
    MessageService(final MessageRepo messageRepo, final MatchProfileRepo matchProfileRepo,
                   final MatchProfileService matchProfileService, final MatcherService matcherService) {
        this.messageRepo = messageRepo;
        this.matchProfileRepo = matchProfileRepo;
        this.matchProfileService = matchProfileService;
        this.matcherService = matcherService;
    }

    public List<PupperMessage> getMessagesWithLimit(int limit) {
        int messageLimit = !isWithinAcceptableMessageLimit(limit) ? MAX_MESSAGES : limit;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, DEFAULT_SORT_ORDER)); //Sorts messages from newest to oldest
        Page<PupperMessage> results = messageRepo.findAll(PageRequest.of(PAGE_NUM, messageLimit, sort));
        int numResults = results.getNumberOfElements();
        LOGGER.info("Number of messages returned: {}", numResults);

        return results.getContent();
    }

    public List<PupperMessage> getCompleteMessageHistoryBetweenMatchProfiles(Long matchProfileId1, Long matchProfileId2) {
        List<PupperMessage> messages = messageRepo.retrieveMessageHistoryOldestToNewest(matchProfileId1, matchProfileId2);

        LOGGER.info("{} total messages were exchanged between matchProfileId={} and matchProfileId={}", messages.size(),
                matchProfileId1, matchProfileId2);
        return messages;
    }

    public List<PupperMessage> getRecentMessageHistoryBetweenMatchProfiles(Long matchProfileId1, Long matchProfileId2) {
        LOGGER.info("Retrieving (up to) 10 most recent messages exchanged between matchProfileId={} and " +
                "matchProfileId={}", matchProfileId1, matchProfileId2);

        return messageRepo.findTop10ByMatchProfileSender_IdAndMatchProfileReceiver_IdOrMatchProfileReceiver_IdAndMatchProfileSender_IdOrderByTimestampDesc(
                matchProfileId1, matchProfileId2, matchProfileId1, matchProfileId2);
    }

    public List<List<PupperMessage>> getRecentMessageHistoriesForAllMatches(Long matchProfileId) {
        List<MatchProfile> matchProfiles = matchProfileService.retrieveMatchesForMatchProfile(matchProfileId);
        List<List<PupperMessage>> allMessageHistories = new ArrayList<>(new ArrayList<>());

        matchProfiles.forEach(each -> allMessageHistories
                .add(messageRepo.retrieve5MostRecentMessagesBetweenMatchProfiles(matchProfileId, each.getId(),
                                                                                each.getId(), matchProfileId)));
        return allMessageHistories;
    }

    public MessageResponse sendMessage(Long senderId, Long receiverId, PupperMessage pupperMessage) {
        if (!pupperMessage.getMatchProfileSender().getId().equals(senderId) ||
                !pupperMessage.getMatchProfileReceiver().getId().equals(receiverId)) {
            return createMessageResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        else if (matcherService.checkForTwoWayMatch(senderId, receiverId) == null) {
            return createMessageResponse(false, null, HttpStatus.BAD_REQUEST,
                    "Error: not a valid match result.");
        }
        if (pupperMessage.getTimestamp() == null) {
            pupperMessage.setTimestamp(Utils.getIsoFormatTimestamp(Instant.now()));
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
     * @param matchProfileId id of matchProfile to delete messages for
     * @return
     */
    public MessageResponse deleteAllMessagesByMatchProfileId(Long matchProfileId) {
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);

        matchProfile.ifPresent(messageRepo::deleteAllMessagesForMatchProfile);

        return matchProfile.isPresent() ?
                createMessageResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION):
                createMessageResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }

    private boolean isWithinAcceptableMessageLimit(int limit) {
        return limit > 0 && limit <= MAX_MESSAGES;
    }



//    /**
//     * Helper method that verifies that two match profiles attempting to exchange messages are a match.
//     * @param matchProfileId1 match profile #1
//     * @param matchProfileId2 match profile #2
//     * @return
//     */
////    private boolean isValidMatchResult(Long matchProfileId1, Long matchProfileId2) {
////        Optional<MatchResult> matchResult1 = matchResultRepo.findByMatchProfileOne_IdAndMatchProfileTwo_Id(matchProfileId1, matchProfileId2);
////        Optional<MatchResult> matchResult2 = matchResultRepo.findByMatchProfileOne_IdAndMatchProfileTwo_Id(matchProfileId2, matchProfileId1);
////
////        if (!matchResult1.isPresent() && !matchResult2.isPresent()) {
////            LOGGER.error("No match result outcome exists between matchProfileId={} and matchProfileId={}", matchProfileId1, matchProfileId2);
////            return false;
////        }
////        return matchResult1
////                .map(m1 -> m1.isMatchForProfileOne() && m1.isMatchForProfileTwo())
////                .orElseGet(() -> matchResult2.get().isMatchForProfileOne() && matchResult2.get().isMatchForProfileTwo());
////    }
}
