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
    Iterable<UserProfile> findAll();
    Iterable<UserProfile> findAll(Sort sort);
    Page<UserProfile> findAll(Pageable pageable);
    Optional<UserProfile> findById (Long id);
    Optional<UserProfile> findByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, Date birthdate);
    UserProfile save(UserProfile userProfile);
    void deleteById(Long id);
    Optional<UserProfile> findByUserAccount(UserAccount userAccount);

    //Search result filter methods

    Page<UserProfile> findByZip (String zipCode, Pageable pageable);
//    Optional<List<UserProfile>> findAllByPupperScore (float score);
//    Optional<List<UserProfile>> findAllByLastLoginBetween (Date start, Date end);
//    Optional<List<UserProfile>> findAllByImage (String imageLocation);
//    Optional<List<UserProfile>> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

}
