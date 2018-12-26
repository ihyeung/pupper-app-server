package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PupperProfileRepo extends PagingAndSortingRepository<PupperProfile, Long> {
    Optional<PupperProfile> findByMatchProfileIdAndName (Long matchProfileId, String name);
    Optional<List<PupperProfile>> findAllByMatchProfileId (Long matchProfileId);
    Optional<List<PupperProfile>> findAllByBreedId(Long breedId);
    List<PupperProfile> findAllByMatchProfile_UserProfile_UserAccount_Username(String userEmail);
    List<PupperProfile> findAllByMatchProfile_UserProfile_Id(Long userProfileId);
    List<PupperProfile> findAllByBreedName(String breed);
    List<PupperProfile> findAllByLifeStage(String lifestage);

    void deleteAllByMatchProfile_Id(Long matchProfileId);
    void deleteAllByMatchProfile_UserProfile_Id(Long userProfileId);

}
