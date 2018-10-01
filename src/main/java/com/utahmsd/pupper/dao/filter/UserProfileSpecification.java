package com.utahmsd.pupper.dao.filter;

import com.utahmsd.pupper.dao.entity.UserProfile;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserProfileSpecification implements Specification<UserProfile> {
    @Override
    public Specification<UserProfile> and(Specification<UserProfile> other) {
        return null;
    }

    @Override
    public Specification<UserProfile> or(Specification<UserProfile> other) {
        return null;
    }

    @Override
    public Predicate toPredicate(Root<UserProfile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
