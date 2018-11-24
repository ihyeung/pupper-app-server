package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface PupperProfileRepo extends JpaRepository<PupperProfile, Long> {
//    Optional<PupperProfile> findById(Long pupId);
//    Optional<List<PupperProfile>> findAllByMatchProfileId(Long matchProfileId);
//    Optional<List<PupperProfile>> findAllByUserId(Long userId); //Find all pups belonging to a given user
//    Optional<List<PupperProfile>> findAllByBreed(String breed);
//    Optional<List<PupperProfile>> findAllByLifeStage(LifeStage lifeStage);

    Optional<PupperProfile> findByMatchProfileIdAndName (Long matchProfileId, String name);
    Optional<List<PupperProfile>> findAllByMatchProfileId (Long matchProfileId);
    Optional<List<PupperProfile>> findAllByBreedId(Long breedId);
    void deleteAllByMatchProfile_Id(Long matchProfileId);

}
