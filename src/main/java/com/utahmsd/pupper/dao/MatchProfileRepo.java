package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MatchProfileRepo extends PagingAndSortingRepository<MatchProfile, Long> {

    Iterable<MatchProfile> findAll();
    Iterable<MatchProfile> findAll(Sort sort);
    Page<MatchProfile> findAllByBreed(Breed breed, Pageable pageable);
    Optional<MatchProfile> findById (Long id);
    Optional<List<MatchProfile>> findAllByUserProfile (UserProfile userProfile);
    MatchProfile save (MatchProfile matchProfile);
    void deleteById(Long id);
    void deleteAllByUserProfile(UserProfile userProfile);

}
