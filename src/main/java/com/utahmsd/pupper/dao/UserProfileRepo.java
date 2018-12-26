package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepo extends PagingAndSortingRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserAccount(UserAccount userAccount);

    Page<UserProfile> findByZip (String zipCode, Pageable pageable);

    UserProfile findByUserAccount_Username(String username);

    @Transactional
    @Modifying
    @Query("UPDATE UserProfile u SET u.lastLogin = :lastLogin WHERE u.id = :id")
    void updateLastLogin(@Param("id") Long userId, @Param("lastLogin") Date lastLogin);

    @Query("FROM UserProfile u WHERE u.zip = :zip")
    List<UserProfile> findAllByZip(@Param("zip") String zip);

    @Query("FROM UserProfile u WHERE u.zip BETWEEN :zipMin AND :zipMax")
    List<UserProfile> findAllByZipBetween(@Param("zipMin") String zipMin, @Param("zipMax") String zipMax);

    @Transactional
    @Modifying
    @Query("UPDATE UserProfile u SET u.profileImage = :url WHERE u.id = :id")
    void updateProfileImage(@Param("id") Long userId, @Param("url") String imageUrl);

}
