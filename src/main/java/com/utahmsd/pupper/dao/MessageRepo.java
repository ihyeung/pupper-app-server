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


    @Query("FROM PupperMessage p WHERE (p.matchProfileSender.id = :id1 AND p.matchProfileReceiver.id = :id2) OR " +
            "(p.matchProfileSender.id = :id2 AND p.matchProfileReceiver.id = :id1) ORDER BY p.timestamp ASC")
    List<PupperMessage> retrieveMessageHistoryOldestToNewest(@Param("id1") Long matchProfileId1, @Param("id2") Long matchProfileId2);

    @Query(value = "SELECT * FROM pupper_message WHERE (from_match_profile_id_fk = ? AND to_match_profile_id_fk = ?) OR " +
            "(from_match_profile_id_fk = ? AND to_match_profile_id_fk = ?) ORDER BY timestamp ASC LIMIT 5", nativeQuery = true)
    List<PupperMessage> retrieve5MostRecentMessagesBetweenMatchProfiles(Long matchProfileId1, Long matchProfileId2,
                                                                         Long matchProfileId3, Long matchProfileId4);

    /**
     * Returns the 10 most recent messages sent from a given matchProfileId to a given matchProfileId.
     * @param matchIdSender
     * @param matchIdReceiver
     * @return
     */
    List<PupperMessage> findTop10ByMatchProfileSender_IdAndMatchProfileReceiver_IdOrderByTimestampDesc(Long matchIdSender, Long matchIdReceiver);

    /**
     * Returns the 10 most recent messages exchanged to or from a given matchProfileId.
     * @param matchIdSender
     * @param matchIdReceiver
     * @return
     */
    List<PupperMessage> findTop10ByMatchProfileSender_IdOrMatchProfileReceiver_IdOrderByTimestampDesc(Long matchIdSender, Long matchIdReceiver);

    /**
     * Retrieves recent (10 messages) message history between two matchProfiles.
     * @param id1 matchProfile #1
     * @param id2 matchProfile #2
     * @param id3 matchProfile #1
     * @param id4 matchProfile #2
     * @return
     */
    List<PupperMessage> findTop10ByMatchProfileSender_IdAndMatchProfileReceiver_IdOrMatchProfileReceiver_IdAndMatchProfileSender_IdOrderByTimestampDesc(Long id1,
                                                                                                                                                        Long id2,
                                                                                                                                                        Long id3,
                                                                                                                                                        Long id4);

    /**
     * Retrieves entire message history between two  matchProfiles.
     * @param id1 matchProfile #1
     * @param id2 matchProfile #2
     * @param id3 matchProfile #1
     * @param id4 matchProfile #2
     * @return
     */
    List<PupperMessage> findAllByMatchProfileSender_IdAndMatchProfileReceiver_IdOrMatchProfileReceiver_IdAndMatchProfileSender_IdOrderByTimestampDesc(Long id1,
                                                                                                                                                        Long id2,
                                                                                                                                                        Long id3,
                                                                                                                                                        Long id4);

    @Transactional
    @Modifying
    @Query("DELETE FROM PupperMessage m WHERE (m.matchProfileSender = :mp1 AND m.matchProfileReceiver = :mp2) OR " +
            "(m.matchProfileReceiver = :mp1 AND m.matchProfileSender = :mp2)")
    void deleteMessageHistoryBetweenMatchProfiles(@Param("mp1") MatchProfile matchProfile1, @Param("mp2") MatchProfile matchProfile2);

    @Transactional
    @Modifying
    @Query("DELETE FROM PupperMessage m WHERE m.matchProfileSender = :mp OR m.matchProfileReceiver = :mp")
    void deleteAllMessagesForMatchProfile(@Param("mp") MatchProfile matchProfile);

}
