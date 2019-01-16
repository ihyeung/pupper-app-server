package com.utahmsd.pupper.service.filter;

import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.PupperProfileResponse.createPupperProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;

@Service
public class PupperProfileFilterService {

    private final PupperProfileRepo pupperProfileRepo;
    private final BreedRepo breedRepo;

    @Autowired
    public PupperProfileFilterService (PupperProfileRepo pupperProfileRepo, BreedRepo breedRepo) {
        this.pupperProfileRepo = pupperProfileRepo;
        this.breedRepo = breedRepo;
    }

    public PupperProfileResponse getPupperProfilesWithFilters(String sort, int limit) {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, sort));
        Iterable<PupperProfile> puppers = pupperProfileRepo.findAll(sortCriteria);
        List<PupperProfile> pupperProfileList = new ArrayList<>();
        if (!puppers.iterator().hasNext()) {
            return createPupperProfileResponse(false, pupperProfileList, HttpStatus.OK, NO_QUERY_RESULTS);
        }

        puppers.forEach(pupperProfile -> {
            if (pupperProfileList.size() < limit) {
                pupperProfileList.add(pupperProfile);
            }
        });
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesFilterByEmail(String userEmail) {
        List<PupperProfile> pupperProfileList =
                pupperProfileRepo.findAllByMatchProfile_UserProfile_UserAccount_Username(userEmail);
        return pupperProfileList.isEmpty() ?
                createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND):
                createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public List<PupperProfile> getPupperProfilesFilterByBreedName(String breed) {
        return pupperProfileRepo.findAllByBreedName(breed);
    }

    public List<PupperProfile> getPupperProfilesFilterByLifeStage(String lifestage) {
        return pupperProfileRepo.findAllByLifeStage(lifestage);
    }

    public PupperProfileResponse getPupperProfilesFilterByBreedId(Long breedId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByBreedId(breedId);
        if (!results.isPresent()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public List<Breed> getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);
        }
        return breedList;
    }

    public Breed getBreedFilterByName(String breed) {
        return breedRepo.findByNameOrAltName(breed, breed);
    }

}
