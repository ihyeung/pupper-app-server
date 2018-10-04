package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dto.pupper.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface BreedRepo extends PagingAndSortingRepository<Breed, Long> {
    Iterable<Breed> findAll(Sort sort);
    Page<Breed> findAll(Pageable pageable);
    Page<Breed> findAllBySize(Size size, Pageable pageable);
    Optional<Breed> findById(Long id);
    Page<Breed> findAllByBreed(Breed breed, Pageable pageable);
    Iterable<Breed> findAllByBreed(Breed breed, Sort sort);
    Breed save(Breed breed);
    void deleteAll(Iterable<? extends Breed> iterable);
    void deleteAll();
    long count();
}
