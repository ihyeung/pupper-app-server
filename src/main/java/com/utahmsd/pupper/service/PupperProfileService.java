package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Named
@Singleton
public class PupperProfileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;

    @Inject
    BreedRepo breedRepo;

    public PupperProfileResponse getAllPupperProfiles() {
        return null;
    }

    public PupperProfileResponse findAllPupperProfilesByUserId(Long userId) {
        return null;
    }

    public PupperProfileResponse findPupperProfile(Long pupperId, Long userId) {
        return null;
    }

    public PupperProfileResponse createNewPupperProfile(PupperProfileRequest request) {
        return null;
    }

    public PupperProfileResponse updatePupperProfile(PupperProfileRequest request) {
        return null;
    }

    public PupperProfileResponse deletePupperProfile(Long userId, Long pupperId) {
        return null;
    }



    public PupperProfileResponse getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "breed"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);
        }
        breedList.forEach(e->System.out.println(e.getBreedName()));
        PupperProfileResponse response = PupperProfileResponse.createPupperProfileResponse(true, Collections.emptyList(), HttpStatus.OK);
        response.setDescription(breedList.toString());
        return response;
    }

}