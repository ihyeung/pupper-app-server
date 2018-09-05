package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepo extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findById (Long id);
    Optional<List<UserProfile>> findAllByLocation (String location);
    Optional<List<UserProfile>> findAllByPupperScore (float score);
    Optional<List<UserProfile>> findAllByLastLoginBetween (Date start, Date end);
    Optional<List<UserProfile>> findAllByImage (String imageLocation);
    Optional<List<UserProfile>> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);




}
