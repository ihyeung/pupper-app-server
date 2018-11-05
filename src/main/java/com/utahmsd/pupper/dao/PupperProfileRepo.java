package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface PupperProfileRepo extends PagingAndSortingRepository<PupperProfile, Long> {
//    Optional<PupperProfile> findById(Long pupId);
//    Optional<List<PupperProfile>> findAllByMatchProfileId(Long matchProfileId);
//    Optional<List<PupperProfile>> findAllByUserId(Long userId); //Find all pups belonging to a given user
//    Optional<List<PupperProfile>> findAllByBreed(String breed);
//    Optional<List<PupperProfile>> findAllByLifeStage(LifeStage lifeStage);

    @NotNull Page<PupperProfile> findAll(@NotNull Pageable pageable);
    Optional<PupperProfile> findById (@NotNull Long id);
    PupperProfile save(@NotNull PupperProfile pupperProfile);
    Optional<List<PupperProfile>> findAllByUserProfile (UserProfile userProfile);
    Optional<List<PupperProfile>> findAllByUserProfileId (Long userProfileId);
    Optional<List<PupperProfile>> findAllByUserProfileIdAndMatchProfileId (Long userProfileId, Long matchProfileId);
    Optional<PupperProfile> findByMatchProfileIdAndName (Long matchProfileId, String name);
    Optional<PupperProfile> findByUserProfileIdAndName (Long userProfileId, String name);
    void deleteById(Long pupperProfileId);
    void deleteAllByUserProfile(UserProfile userProfile);
    void deleteAllByUserProfileId(Long userProfileId);

}
