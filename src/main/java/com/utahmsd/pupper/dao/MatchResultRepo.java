package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
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

    @Query("DELETE FROM MatchResult m WHERE m.recordExpires < :now AND " +
            "(m.matchForProfileOne IS NULL OR m.matchForProfileTwo IS NULL) " +
            "OR (m.resultCompleted IS NULL)")
    void deleteIncompleteExpiredMatcherRecords(@Param("now") Instant currentInstant);

    @Query("FROM MatchResult m WHERE (m.resultCompleted IS NOT NULL OR " +
            "(m.matchForProfileOne IS NOT NULL AND m.matchForProfileTwo IS NOT NULL)) " +
            "AND (m.matchProfileOne.id = :id OR m.matchProfileTwo.id = :id)")
    List<MatchResult> findCompletedMatchResultsForMatchProfile(@Param("id") Long matchProfileId);

    @Query("SELECT COUNT(m) FROM MatchResult m WHERE " +
            "(m.matchForProfileOne = true AND m.matchForProfileTwo = true) " +
            "AND (m.matchProfileOne.id = :id OR m.matchProfileTwo.id = :id)")
    Integer getMutualMatchesCount(@Param("id") Long matchProfileId);

    @Query("SELECT COUNT(m) FROM MatchResult m WHERE " +
            "(m.matchProfileOne.id = :id AND m.matchForProfileOne = true AND m.matchForProfileTwo IS NOT NULL) " +
            "OR (m.matchProfileTwo.id = :id AND m.matchForProfileTwo = true AND m.matchForProfileOne IS NOT NULL)")
    Integer getLikesByMatchProfileId(@Param("id") Long matchProfileId);

    @Transactional
    @Modifying
    @Query("UPDATE MatchResult m set m.recordExpires = :expiresAt WHERE " +
            "(m.matchForProfileOne IS NULL OR m.matchForProfileTwo IS NULL) OR (m.resultCompleted is null)")
    void extendExpirationForIncompleteRecords(@Param("expiresAt") Instant expiresAt);

    @Transactional
    @Modifying
    @Query("DELETE FROM MatchResult m WHERE (m.matchProfileOne.id= :id1 AND m.matchProfileTwo.id = :id2)" +
            " OR (m.matchProfileOne.id = :id2 AND m.matchProfileTwo.id = :id1)")
    void deleteMatchResultByMatchProfileIds(@Param("id1") Long id1, @Param("id2") Long id2);

    void deleteMatchResultByMatchProfileOne_IdOrMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);

    @Query("SELECT COUNT(m) FROM MatchResult m WHERE m.recordExpires < :now AND " +
            "(m.matchForProfileOne is null or m.matchForProfileTwo IS NULL) " +
            "OR (m.resultCompleted is null)")
    Integer getExpiredIncompleteRecordsCount(@Param("now") Instant now);

    /**
     * Alternative method for retrieving matchProfiles previously rated for a given matchProfileId, in a single
     * native query (as opposed to combining the results of findActivePreviouslyRatedMatchProfiles and
     * findPassivePreviouslyRatedMatchProfiles into a hashset, WITH THE EXCEPTION that it retrieves a list of IDs
     * as opposed to a list of MatchProfiles.
     * @param matchProfileId
     * @param matchProfileId1
     * @return
     */

    //NOTE: IF USING A NATIVE QUERY, match_profile_id_fk columns are of type Integer

    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ? " +
            "and match_profile_1_result is not null) " +
            "union (select match_profile_id_fk_1 from match_result where (match_profile_id_fk_2 = ? " +
            "and match_profile_2_result is not null))", nativeQuery = true)
    List<Integer> retrieveAllIdsforMatchProfilesPreviouslyRated(Long matchProfileId, Long matchProfileId1);

    //NOTE: IF USING JPQL, matchProfileTwo.id is of type Long, not Integer like in a native query.
    @Query("select r.matchProfileTwo.id from MatchResult r where r.matchProfileOne.id = :id " +
            "and ((r.matchForProfileOne is null and r.batchSent is not null and r.recordExpires > :now ) OR " +
            " r.resultCompleted is not null)")
    List<Long> findMatchProfilesInPreviouslySentBatchesNotExpired(@Param("id") Long matchProfileId,
                                                                     @Param("now") Instant now);

    @Query(value = "(select match_profile_id_fk_2 from match_result where match_profile_id_fk_1 = ? " +
            "and match_profile_1_result = true and match_profile_2_result = true) " +
            "union (select match_profile_id_fk_1 from match_result where match_profile_id_fk_2 = ? " +
            "and match_profile_1_result = true and match_profile_2_result = true)" , nativeQuery = true)
    List<Integer> retrieveIdsOfAllMutualMatches(Long matchProfileId, Long matchProfileId1);
}
