package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MatchProfileRepo extends JpaRepository<MatchProfile, Long> {
    Page<MatchProfile> findAllByBreed(Breed breed, Pageable pageable);
    Optional<List<MatchProfile>> findAllByUserProfile (UserProfile userProfile);
    Optional<MatchProfile> findByUserProfileIdAndId(Long userProfileId, Long matchProfileId);
    Optional<List<MatchProfile>> findAllByUserProfile_Id (Long userProfileId);
    Optional<MatchProfile> findByUserProfileIdAndBreedAndEnergyLevelAndLifeStage(Long userId, Breed breed, Energy energy,
                                                                                  LifeStage lifeStage);
    void deleteAllByUserProfile_Id(Long userProfileId);

}
