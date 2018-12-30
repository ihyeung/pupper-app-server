package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepo extends PagingAndSortingRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserAccount(UserAccount userAccount);

    UserProfile findByUserAccount_Username(String username);

    @Transactional
    @Modifying
    @Query("UPDATE UserProfile u SET u.lastLogin = :lastLogin WHERE u.id = :id")
    void updateLastLogin(@Param("id") Long userId, @Param("lastLogin") Date lastLogin);

    @Query("FROM UserProfile u WHERE u.zip = :zip")
    List<UserProfile> findAllByZip(@Param("zip") String zip);

    @Transactional
    @Modifying
    @Query("UPDATE UserProfile u SET u.profileImage = :url WHERE u.id = :id")
    void updateProfileImage(@Param("id") Long userId, @Param("url") String imageUrl);

    @Transactional
    void deleteByUserAccount_Username(String username);

}
