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
    @Query("UPDATE MatchResult m SET m.matchForProfileTwo = :isMatch, m.resultCompleted = :updatedAt " +
            "WHERE m.matchProfileOne.id= :id1 AND m.matchProfileTwo.id = :id2")
    void updateMatchResult(@Param("isMatch") Boolean isMatchForTwo,
                            @Param("updatedAt") Instant lastUpdated,
                            @Param("id1") Long matchProfileOne,
                            @Param("id2") Long matchProfileTwo);

    @Query("FROM MatchResult m WHERE (m.matchProfileOne.id = :id1 AND m.matchProfileTwo.id = :id2) OR " +
            "(m.matchProfileOne.id = :id2 AND m.matchProfileTwo.id = :id1) ")
    MatchResult findMatchResult(@Param("id1") Long matchProfileId1, @Param("id2") Long matchProfileId2);


    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ?) union " +
                    " (select match_profile_id_fk_1 from match_result where (match_profile_id_fk_2 = ? " +
            "and match_profile_2_result is not null))", nativeQuery = true)
    List<MatchProfile> getDistinctViewedMatchProfilesList(Long matchProfileId, Long matchProfileId1);

    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ? " +
            "AND match_profile_1_result = TRUE AND match_profile_2_result = TRUE) union " +
            " (select match_profile_id_fk_1 from match_result where match_profile_id_fk_2 = ? " +
            "AND match_profile_1_result = TRUE AND match_profile_2_result = TRUE)", nativeQuery = true)
    List<Long> findAllMatches(Long matchProfileId, Long matchProfileId1);


    @Query("FROM MatchResult m WHERE m.batchSent IS NOT NULL AND m.resultCompleted IS NULL AND m.recordExpires < :now")
    List<MatchResult> findIncompleteExpiredRecordsToDelete(@Param("now") Instant currentInstant);

    /**
     * Finds all match results where the matchProfileId was the initiating user who generated the match_result record.
     * i.e., The matchProfile corresponding to matchProfileId was shown and rated the other match_profile first.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileTwo from MatchResult r where r.matchProfileOne.id = :id")
    List<MatchProfile> findActiveMatcherResults(@Param("id") Long matchProfileId);

    //    and r.batchSent is not null and r.recordExpires > :currentTime

    /**
     * Finds all completed passive match result records (i.e., another user initiated creating the match_result record)
     * i.e., The matchProfile corresponding to matchProfileId was shown and rated the other matchProfile AFTER the
     * other matchProfile had already rated them.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileOne from MatchResult r where r.matchProfileTwo.id = :id " +
            "and r.matchForProfileTwo is not null")

//   OR r.matchForProfileTwo is null and r.resultCompleted is not null

    List<MatchProfile> findPassiveMatcherResults(@Param("id") Long matchProfileId);

    /**
     * Finds all active matches for a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileTwo from MatchResult r where r.matchProfileOne.id = :id " +
            "AND r.matchForProfileOne = TRUE AND r.matchForProfileTwo = TRUE")
    List<MatchProfile> findActiveMatches(@Param("id") Long matchProfileId);

    /**
     * Finds all passive matches for a given matchProfileId.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileOne from MatchResult r where r.matchProfileTwo.id = :id " +
            "AND (r.matchForProfileOne = true AND r.matchForProfileTwo = true)")
    List<MatchProfile> findPassiveMatches(@Param("id") Long matchProfileId);

}
