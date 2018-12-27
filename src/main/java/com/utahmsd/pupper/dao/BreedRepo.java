package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dto.pupper.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreedRepo extends PagingAndSortingRepository<Breed, Long> {

    Breed findByNameOrAltName(String name, String altName);

    List<Breed> findAllBySize(Size size);

    /**
     * Method to be used in the future for looking up a dog's size from the breed.
     * @param breed
     * @return
     */
    @Query("SELECT b.size FROM Breed b WHERE b.name = :breed OR b.altName = :breed")
    Size getBreedSizeByName(@Param("breed") String breed);
}
