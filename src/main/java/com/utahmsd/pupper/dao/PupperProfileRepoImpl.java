package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.LifeStage;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Named
@Singleton
public class PupperProfileRepoImpl extends RepositoryImpl<Pupper, Long> implements PupperProfileRepo {


    public PupperProfileRepoImpl() {
        entityClass = Pupper.class;

    }

//        public PupperProfileRepoImpl(Class<Pupper> pupperClass) {
//        super(Pupper.class);
//    }

    @Override
    public List<Pupper> findAllByUserId(Long userId) {
        CriteriaQuery<Pupper> c = entityManager.getCriteriaBuilder().createQuery(Pupper.class);
        c.where(entityManager.getCriteriaBuilder().equal(c.from(Pupper.class).get("pupper_id"), userId));
        return entityManager.createQuery(c).getResultList();
    }

    @Override
    public List<Pupper> findAllByBreed(String breed) {
        return null;
    }

    @Override
    public List<Pupper> findAllByLifestage(LifeStage lifeStage) {
        return null;
    }

}
