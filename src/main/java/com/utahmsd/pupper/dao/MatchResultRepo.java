package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchResultRepo extends JpaRepository<MatchResult, Long> {
    //    Optional<MatchResult> findByMatchForProfileOneIsTrueAndMatchForProfileTwoIsTrue(Long matchProfileId1, Long matchProfileId2);
    Optional<MatchResult> findByMatchProfileOne_IdAndMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);
    void deleteByMatchProfileOne_IdOrMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);

//    @Transactional
//    @Modifying
//    @Query("UPDATE MatchResult m SET m.matchForProfileOne = :isMatch, m.lastUpdateToMatchResult = :lastUpdate WHERE m.matchProfileOne. = :profileId AND ")
//    void insertMatchResults(@Param("isMatch") boolean isMatchForOne,
//                            @Param("isMatch2") boolean isMatchForTwo,
//                            @Param("lastUpdate") String lastUpdate,
//                            @Param("profileId") Long matchProfileOneId,
//                            @Param("profileId2") Long matchProfileTwoId);

    @Query("FROM MatchResult m WHERE (m.matchProfileOne.id = :id1 AND m.matchProfileTwo.id = :id2) OR " +
            "(m.matchProfileOne.id = :id2 AND m.matchProfileTwo.id = :id1) ")
    MatchResult findMatchResult(@Param("id1") Long matchProfileId1, @Param("id2") Long matchProfileId2);

    /**
     * Finds all match results where the matchProfileId was the initiating user who generated the match_result record.
     * i.e., The matchProfile corresponding to matchProfileId was shown and rated the other match_profile first.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileTwo from MatchResult r where r.matchProfileOne.id = :id")
    List<MatchProfile> findActiveMatcherResults(@Param("id") Long matchProfileId);

    /**
     * Finds all passive match result records (i.e., another user initiated creating the match_result record)
     * i.e., The matchProfile corresponding to matchProfileId was shown and rated the other matchProfile AFTER the
     * other matchProfile had already rated them.
     * @param matchProfileId
     * @return
     */
    @Query("select r.matchProfileOne from MatchResult r where r.matchProfileTwo.id = :id")
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
