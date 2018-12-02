package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
