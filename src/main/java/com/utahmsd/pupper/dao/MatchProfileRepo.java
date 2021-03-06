package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchProfileRepo extends JpaRepository<MatchProfile, Long> {

    Optional<MatchProfile> findByUserProfileIdAndId(Long userProfileId, Long matchProfileId);

    Optional<List<MatchProfile>> findAllByUserProfile_Id (Long userProfileId);

    @Query("FROM MatchProfile m WHERE m.userProfile.lastLogin >= :date ORDER BY m.score DESC")
    List<MatchProfile> findMatchProfilesWithRecentActivityOrderByScore(@Param("date") Date beforeDate, Pageable pageable);

    @Query("SELECT m FROM MatchProfile m WHERE m.userProfile.zip = :zip ORDER BY m.userProfile.lastLogin")
    List<MatchProfile> findAllByZip(@Param("zip") String zip);

    @Query("FROM MatchProfile m JOIN UserProfile u ON m.userProfile = u WHERE m.userProfile.userAccount.username = :username")
    List<MatchProfile> findAllByUserEmail(@Param("username") String username);

    List<MatchProfile> findMatchProfilesByBreed_Name(String breed);

    MatchProfile findByNamesEquals(String names);

    @Transactional
    @Modifying
    @Query("DELETE FROM MatchProfile m WHERE m.userProfile.id = :userId")
    void deleteMatchProfilesByUserProfile_Id(@Param("userId") Long userProfileId);

    List<MatchProfile> findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(List<Long> idsToFilter, Long matchProfileId);

    List<MatchProfile> findTop3ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(List<Long> matchProfileIds,
                                                                                           Long id, List<String> zips);

    List<MatchProfile> findTop3ByIdIsNotInAndIdIsNotOrderByScoreDesc(List<Long> matchProfileIds, Long id);

    ///////// Database queries to use specifically if getIdsOfPreviouslyShownProfilesForMatchProfile() result is empty //////
    List<MatchProfile> findTop3ByIdIsNotOrderByScoreDesc(Long id);
    List<MatchProfile> findTop3ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(Long id, List<String> zipCodesInRadius);

}
