package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.pup.LifeStage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PupperProfileRepo extends CrudRepository<PupperProfile, Long> {
    Optional<PupperProfile> findById(Long pupId);
    Optional<List<PupperProfile>> findAllByMatchProfileId(Long matchProfileId);
    Optional<List<PupperProfile>> findAllByUserId(Long userId); //Find all pups belonging to a given user
    Optional<List<PupperProfile>> findAllByBreed(String breed);
    Optional<List<PupperProfile>> findAllByLifeStage(LifeStage lifeStage);
}
