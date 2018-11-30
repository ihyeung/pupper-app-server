package com.utahmsd.pupper.service.filter;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static com.utahmsd.pupper.dto.PupperProfileResponse.createPupperProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;

@Named
@Singleton
public class PupperProfileFilterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PupperProfileFilterService.class);

    private final PupperProfileRepo pupperProfileRepo;
    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    PupperProfileFilterService (PupperProfileRepo pupperProfileRepo, MatchProfileRepo matchProfileRepo) {
        this.pupperProfileRepo = pupperProfileRepo;
        this.matchProfileRepo = matchProfileRepo;
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

    public PupperProfileResponse getPupperProfilesFilterByBreedName(String breedName) {
        List<PupperProfile> results = pupperProfileRepo.findAllByBreedName(breedName);
        if (results == null || results.isEmpty()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }

        return createPupperProfileResponse(true, results, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesFilterByEmail(String userEmail) {
        List<PupperProfile> pupperProfileList = pupperProfileRepo.findAllByMatchProfile_UserProfile_UserAccount_Username(userEmail);
        if (pupperProfileList == null || pupperProfileList.isEmpty()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
