package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dto.pupper.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface BreedRepo extends JpaRepository<Breed, Long> {
//    Iterable<Breed> findAll(Sort sort);
//    Page<Breed> findAll(Pageable pageable);
    Page<Breed> findAllBySize(Size size, Pageable pageable);
//    Optional<Breed> findById(Long id);
    Page<Breed> findAllByName(String breed, Pageable pageable);
    Iterable<Breed> findAllByName(String breed, Sort sort);
//    Breed save(Breed breed);
//    void deleteAll(Iterable<? extends Breed> iterable);
//    void deleteAll();
//    long count();
}
