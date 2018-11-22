package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Optional;

public interface UserProfileRepo extends PagingAndSortingRepository<UserProfile, Long> {
    Optional<UserProfile> findByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, Date birthdate);
    Optional<UserProfile> findByUserAccount(UserAccount userAccount);
    Page<UserProfile> findByZip (String zipCode, Pageable pageable);

//    Optional<List<UserProfile>> findAllByPupperScore (float score);
//    Optional<List<UserProfile>> findAllByLastLoginBetween (Date start, Date end);
//    Optional<List<UserProfile>> findAllByImage (String imageLocation);
//    Optional<List<UserProfile>> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

}
