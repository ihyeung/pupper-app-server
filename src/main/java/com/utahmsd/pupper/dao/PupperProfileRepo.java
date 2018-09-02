package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.LifeStage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PupperProfileRepo extends CrudRepository<Pupper, Long> {
//    List<Pupper> findAllByUserId(Long userId);
//    List<Pupper> findAllByBreed(String breed);
//    List<Pupper> findAllByLifestage(LifeStage lifeStage);
}
