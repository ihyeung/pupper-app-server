package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MatchProfileRepo extends JpaRepository<MatchProfile, Long> {
    Optional<MatchProfile> findByUserProfileIdAndId(Long userProfileId, Long matchProfileId);
    Optional<List<MatchProfile>> findAllByUserProfile_Id (Long userProfileId);

    @Query("FROM MatchProfile m WHERE m.userProfile.lastLogin >= :date ORDER BY m.score DESC")
    List<MatchProfile> findMatchProfilesWithRecentActivityOrderByScore(@Param("date") Date beforeDate, Pageable pageable);

    @Query("SELECT m FROM MatchProfile m WHERE m.userProfile.zip = :zip ORDER BY m.userProfile.lastLogin")
    List<MatchProfile> findAllByZip(@Param("zip") String zip);

    List<MatchProfile> findAllByIdIn(List<Long> ids);

    @Query("SELECT m FROM MatchProfile m WHERE m.userProfile.lastLogin BETWEEN :lastLoginStart AND :lastLoginEnd")
    List<MatchProfile> findAllByUserProfileLastLogin(@Param("lastLoginStart") Date dateStart, @Param("lastLoginEnd") Date dateEnd);

    @Query("FROM MatchProfile m JOIN UserProfile u ON m.userProfile = u WHERE m.userProfile.userAccount.username = :username")
    List<MatchProfile> findAllByUserEmail(@Param("username") String username);

    List<MatchProfile> findMatchProfilesByBreed_Name(String breed);

    MatchProfile findByNamesEquals(String names);

    @Transactional
    @Modifying
    @Query("DELETE FROM MatchProfile m WHERE m.userProfile.id = :userId")
    void deleteMatchProfilesByUserProfile_Id(@Param("userId") Long userProfileId);

    List<MatchProfile> findAllByIdIsNotInAndIdIsNotOrderByScoreDesc(List<Long> idsToFilter, Long matchProfileId);

    List<MatchProfile> findAllByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(List<Long> idsToFilter,
                                                                                            Long matchProfileId,
                                                                                            List<String> zipcodes);

    List<MatchProfile> findAllByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(Long matchProfileId, List<String> zips);

    /**
     * Retrieves the next 5 matchProfiles that have not yet been displayed to the user, that are located within
     * a predefined radius of the user.
     * @param idsToFilter a list of ids corresponding to matchProfiles that the user has already seen.
     * @param matchProfileId the user's matchProfileId
     * @param zipcodes a list of valid zipcodes within a given radius of the user's location.
     * @return
     */
    List<MatchProfile> findTop5ByIdIsNotInAndIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(List<Long> idsToFilter,
                                                                                           Long matchProfileId,
                                                                                           List<String> zipcodes);

    /**
     * Retrieves the next 5 matchProfiles that have not yet been displayed to the user, that are located within
     * a predefined radius of the user.
     *
     * Results are returned in descending order of score.
     * @param matchProfileId matchProfileId retrieving the batch of profiles
     * @param zips a list of zip codes that fall within a predefined radius of the user's zip code.
     * @return
     */
    List<MatchProfile> findTop5ByIdIsNotAndUserProfile_ZipIsInOrderByScoreDesc(Long matchProfileId, List<String> zips);

}
