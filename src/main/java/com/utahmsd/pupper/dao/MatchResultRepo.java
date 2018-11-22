package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MatchResultRepo extends PagingAndSortingRepository<MatchResult, Long> {

    //    Optional<MatchResult> findByMatchForProfileOneIsTrueAndMatchForProfileTwoIsTrue(Long matchProfileId1, Long matchProfileId2);
    Optional<MatchResult> findByMatchProfileOne_IdAndMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);
    MatchResult save(MatchResult matchResult);
    void deleteByMatchProfileOne_IdOrMatchProfileTwo_Id(Long matchProfileId1, Long matchProfileId2);
}
