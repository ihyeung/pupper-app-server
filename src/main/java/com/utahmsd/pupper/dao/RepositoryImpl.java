//package com.utahmsd.pupper.dao;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import java.io.Serializable;
//import java.sql.ResultSet;
//import java.util.List;
//import java.util.Optional;
//
//public class RepositoryImpl<T, ID extends Serializable> implements Repository <T, ID> {
//
//    @PersistenceContext
//    EntityManager entityManager;
//
//    public Class<T> entityClass;
//
//    public RepositoryImpl(){
//        entityClass = null;
//    }
//
//    public RepositoryImpl(Class<T> entityClass) {
//        this.entityClass = entityClass;
//    }
//
//    @Override
//    public Optional<T> find(ID id) {
//       return null;
//    }
//
//    @Override
//    public Optional<List<T>> findAll() {
//        CriteriaQuery<T> c = entityManager.getCriteriaBuilder().createQuery(entityClass);
//        c.select(c.from(entityClass));
//        return Optional.of(entityManager.createQuery(c).getResultList());
//    }
//
//    @Override
//    public void save(T o) {
//        entityManager.persist(o);
//    }
//
//    @Override
//    public Optional<T> update(T o) {
//        return Optional.of(entityManager.merge(o));
//    }
//
//    @Override
//    public void delete(T o) {
//        entityManager.remove(o);
//    }
//}
