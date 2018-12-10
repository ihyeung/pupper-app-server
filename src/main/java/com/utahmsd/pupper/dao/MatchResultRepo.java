package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface MatchResultRepo extends JpaRepository<MatchResult, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO match_result (match_profile_id_fk_1, match_profile_id_fk_2, match_profile_1_result, " +
            "batch_sent, record_expires) VALUES (?, ?, ?, ?, ?)", nativeQuery = true)
    void insertMatchResult(Long matchProfileId, Long matchProfileId2, Boolean isMatchForOne, Instant batchSent, Instant expires);

    @Transactional
    @Modifying
    @Query("UPDATE MatchResult m SET m.matchForProfileOne = :isMatch " +
            "WHERE m.matchProfileOne.id= :id1 AND m.matchProfileTwo.id = :id2")
    void updateMatchResultByMatchProfileOne(@Param("isMatch") Boolean isMatchForMatchProfileOne,
                                            @Param("id1") Long matchProfileOne,
                                            @Param("id2") Long matchProfileTwo);

    @Transactional
    @Modifying
    @Query("UPDATE MatchResult m SET m.matchForProfileTwo = :isMatch " +
            "WHERE m.matchProfileOne.id= :id1 AND m.matchProfileTwo.id = :id2")
    void updateMatchResultByMatchProfileTwo(@Param("isMatch") Boolean isMatchForMatchProfileTwo,
                                            @Param("id1") Long matchProfileOne,
                                            @Param("id2") Long matchProfileTwo);
    @Transactional
    @Modifying
    @Query("UPDATE MatchResult m SET m.resultCompleted = :now WHERE m.id = :id")
    void markMatchResultAsCompleted(@Param("id") Long matchResultId, @Param("now") Instant now);


    @Query("FROM MatchResult m WHERE (m.matchProfileOne.id = :id1 AND m.matchProfileTwo.id = :id2) OR " +
            "(m.matchProfileOne.id = :id2 AND m.matchProfileTwo.id = :id1) ")
    MatchResult findMatcherRecord(@Param("id1") Long matchProfileId1, @Param("id2") Long matchProfileId2);

    //TODO: FIX THIS QUERY SO IT STOPS THROWING AN EXCEPTION
    @Query(value = "(select distinct match_profile_id_fk_2 from match_result " +
            "where (match_profile_id_fk_1 = ?1 " +
            "and match_profile_1_result is not null) " +
            "AND ((match_result_completed IS NOT NULL) OR (batch_sent is not null and record_expires > ?2))) " +
            "union (select distinct match_profile_id_fk_1 from match_result " +
            "where (match_profile_id_fk_2 = ?3 " +
            "and match_profile_2_result is not null) " +
            "AND ((match_result_completed is not null) " +
            "OR (batch_sent is not null)))", nativeQuery = true)
    List<Long> retrieveAllIdsforMatchProfilesSentInPreviousBatchesAndNotExpired(Long matchProfileId, Instant now,
                                                                                Long matchProfileId1, Instant now1);

    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ? " +
            "AND match_profile_1_result = TRUE AND match_profile_2_result = TRUE) union " +
            " (select match_profile_id_fk_1 from match_result where match_profile_id_fk_2 = ? " +
            "AND match_profile_1_result = TRUE AND match_profile_2_result = TRUE)", nativeQuery = true)
    List<Long> retrieveMatchProfileIdsOfAllMatches(Long matchProfileId, Long matchProfileId1);

    @Query("DELETE FROM MatchResult m WHERE m.resultCompleted IS NULL AND m.recordExpires < :now")
    void deleteIncompleteExpiredMatcherRecords(@Param("now") Instant currentInstant);

    @Query("FROM MatchResult m WHERE (m.resultCompleted IS NOT NULL OR " +
            "(m.matchForProfileOne IS NOT NULL AND m.matchForProfileTwo IS NOT NULL)) " +
            "AND (m.matchProfileOne.id = :id OR m.matchProfileTwo.id = :id)")
    List<MatchResult> findCompletedMatchResultsForMatchProfile(@Param("id") Long matchProfileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MatchResult m WHERE (m.matchProfileOne.id= :id1 AND m.matchProfileTwo.id = :id2)" +
            " OR (m.matchProfileOne.id = :id2 AND m.matchProfileTwo.id = :id1)")
    void deleteMatchResultByMatchProfileIds(@Param("id1") Long id1, @Param("id2") Long id2);

    void deleteMatchResultByMatchProfileOne_IdOrMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);

    /**
     * Finds all profiles for which the corresponding match_result was a mutual match for a given matchProfileId,
     * where this matchProfileId was the first to retrieve a matcher batch containing the other.
     */
    @Query("select r.matchProfileTwo from MatchResult r where r.matchProfileOne.id = :id " +
            "AND r.matchForProfileOne = TRUE AND r.matchForProfileTwo = TRUE")
    List<MatchProfile> findActiveMatches(@Param("id") Long matchProfileId);

    /**
     * Finds all profiles for which the corresponding match_result was a mutual match for a given matchProfileId,
     * where this matchProfileId was NOT the first to retrieve a matcher batch containing the other user.
     */
    @Query("select r.matchProfileOne from MatchResult r where r.matchProfileTwo.id = :id " +
            "AND (r.matchForProfileOne = true AND r.matchForProfileTwo = true)")
    List<MatchProfile> findPassiveMatches(@Param("id") Long matchProfileId);

    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ? " +
            "and match_profile_1_result is not null) " +
            "union (select match_profile_id_fk_1 from match_result where (match_profile_id_fk_2 = ? " +
            "and match_profile_2_result is not null))", nativeQuery = true)
    List<Long> retrieveAllIdsforMatchProfilesPreviouslyRated(Long matchProfileId, Long matchProfileId1);

    /**
     * Finds all match results (both incomplete/complete) where the matchProfileId was the initiating user who generated
     * the match_result record i.e., The matchProfile corresponding to matchProfileId was shown and rated the
     * other match_profile first.
     */
    @Query("select r.matchProfileTwo from MatchResult r where r.matchProfileOne.id = :id " +
            "and r.matchForProfileOne IS NOT NULL")
    List<MatchProfile> findActiveMatcherResults(@Param("id") Long matchProfileId);

    /**
     * Finds all PASSIVE match result records for profiles for which a given matchProfile has rated.
     * i.e., The matchProfile corresponding to matchProfileId was shown and rated the other matchProfile AFTER the
     * other matchProfile had already rated them.
     */
    @Query("select r.matchProfileOne from MatchResult r where r.matchProfileTwo.id = :id " +
            "and r.matchForProfileTwo is not null")
    List<MatchProfile> findPassiveMatcherResults(@Param("id") Long matchProfileId);

}
