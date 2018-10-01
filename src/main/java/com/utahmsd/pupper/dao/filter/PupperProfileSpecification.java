package com.utahmsd.pupper.dao.filter;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PupperProfileSpecification implements Specification<PupperProfile> {
    @Override
    public Specification<PupperProfile> and(Specification<PupperProfile> other) {
        return null;
    }

    @Override
    public Specification<PupperProfile> or(Specification<PupperProfile> other) {
        return null;
    }

    @Override
    public Predicate toPredicate(Root<PupperProfile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
