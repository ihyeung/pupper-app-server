package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends PagingAndSortingRepository<PupperMessage, Long> {
    Optional<List<PupperMessage>> findAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(Long matchProfileId1, Long matchProfileId2);
    Optional<List<PupperMessage>> findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(Long matchProfileId1, Long matchProfileId2);


    @Query("FROM PupperMessage p WHERE (p.matchProfileSender.id = :id1 OR p.matchProfileReceiver.id = :id1) AND " +
            "(p.matchProfileSender.id = :id2 OR p.matchProfileReceiver.id = :id2) ORDER BY p.timestamp DESC")
    List<PupperMessage> retrieveMessageHistoryNewestToOldest(@Param("id1") Long matchProfileId1, @Param("id2") Long matchProfileId2);

    /**
     * Returns the 25 most recent messages sent from a given matchProfileId to a given matchProfileId.
     * @param matchIdSender
     * @param matchIdReceiver
     * @return
     */
    List<PupperMessage> findTop25ByMatchProfileSender_IdAndMatchProfileReceiver_IdOrderByTimestampDesc(Long matchIdSender, Long matchIdReceiver);

    /**
     * Returns the 25 most recent messages exchanged to/from a given matchProfileId.
     * @param matchIdSender
     * @param matchIdReceiver
     * @return
     */
    List<PupperMessage> findTop25ByMatchProfileSender_IdOrMatchProfileReceiver_IdOrderByTimestampDesc(Long matchIdSender, Long matchIdReceiver);


    void deleteAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(Long matchProfileSenderId, Long matchProfileReceiverId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PupperMessage m WHERE (m.matchProfileSender = :mp1 AND m.matchProfileReceiver = :mp2) OR (m.matchProfileReceiver = :mp1 AND m.matchProfileSender = :mp2)")
    void deleteMessageHistoryBetweenMatchProfiles(@Param("mp1") MatchProfile matchProfile1, @Param("mp2") MatchProfile matchProfile2);

    @Transactional
    @Modifying
    @Query("DELETE FROM PupperMessage m WHERE m.matchProfileSender = :mp OR m.matchProfileReceiver = :mp")
    void deleteAllMessagesForMatchProfile(@Param("mp") MatchProfile matchProfile);

}
