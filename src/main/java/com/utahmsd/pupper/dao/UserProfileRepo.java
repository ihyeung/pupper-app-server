package com.utahmsd.pupper.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepo extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findById (Long id);
    Optional<List<UserProfile>> findAllByLocation (String location);
    Optional<List<UserProfile>> findAllByPupperScoreWithinRange (float min, float max);
    Optional<List<UserProfile>> findAllByLastLoginBetween (Date start, Date end);
    Optional<List<UserProfile>> findAllByImageExists (String imageLocation);
    Optional<List<UserProfile>> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);




}
