package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.pupper.PreferenceType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MatchPreferenceRepo extends PagingAndSortingRepository<MatchPreference, Long> {
    List<MatchPreference> findAllByMatchProfile_Id(Long matchProfileId);

    List<MatchPreference> findAllByMatchProfile_IdAndPreferenceType(Long matchProfileId, PreferenceType preferenceType);

    MatchPreference findByMatchProfile_IdAndId(Long matchProfileId, Long matchPreferenceId);

    @Transactional
    @Modifying
    @Query("UPDATE MatchPreference mp SET mp.preferenceType = :preferenceType, mp.matchingPreference = :preference WHERE mp.id = :id")
    void updateMatchPreference(@Param("preferenceType") PreferenceType type, @Param("preference") String preference,
                               @Param("id") Long matchPreferenceId);

    void deleteAllByMatchProfile_Id(Long matchProfileId);
}
