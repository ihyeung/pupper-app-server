package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PupperProfileRepo extends PagingAndSortingRepository<PupperProfile, Long> {
//    Optional<PupperProfile> findById(Long pupId);
//    Optional<List<PupperProfile>> findAllByMatchProfileId(Long matchProfileId);
//    Optional<List<PupperProfile>> findAllByUserId(Long userId); //Find all pups belonging to a given user
//    Optional<List<PupperProfile>> findAllByBreed(String breed);
//    Optional<List<PupperProfile>> findAllByLifeStage(LifeStage lifeStage);

    Iterable<PupperProfile> findAll();
    Iterable<PupperProfile> findAll(Sort sort);
    Page<PupperProfile> findAll(Pageable pageable);
    Optional<PupperProfile> findById (Long id);
    PupperProfile save(PupperProfile pupperProfile);
    Optional<List<PupperProfile>> findAllByUserProfile (UserProfile userProfile);
    Optional<List<PupperProfile>> findAllByMatchProfile (MatchProfile matchProfile);
    void deleteById(Long id);
    void deleteAllByUserProfile(UserProfile userProfile);
}
