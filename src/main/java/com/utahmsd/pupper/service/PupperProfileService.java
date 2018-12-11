package com.utahmsd.pupper.service;

import com.amazonaws.util.StringUtils;
import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dto.BreedResponse;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.BreedResponse.createBreedResponse;
import static com.utahmsd.pupper.dto.PupperProfileResponse.createPupperProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static com.utahmsd.pupper.util.ProfileUtils.dobToLifeStage;

@Named
@Singleton
public class PupperProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final String DEFAULT_SORT = "breed";
    private final String PUPPER_PROFILE = "Pupper profile";
    private final String INVALID_OR_EMPTY_USER_PROFILE_ID = "No pupper profiles belonging to user profile id='%d' were found.";
    private final String EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID =
            "No pupper profiles belonging to user profile id %d and match profile %d were found.";

    private final PupperProfileRepo pupperProfileRepo;
    private final MatchProfileRepo matchProfileRepo;
    private final BreedRepo breedRepo;

    @Autowired
    PupperProfileService (PupperProfileRepo pupperProfileRepo, MatchProfileRepo matchProfileRepo, BreedRepo breedRepo) {
        this.pupperProfileRepo = pupperProfileRepo;
        this.matchProfileRepo = matchProfileRepo;
        this.breedRepo = breedRepo;
    }

    public PupperProfileResponse getAllPupperProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT));
        Iterable<PupperProfile> puppers = pupperProfileRepo.findAll(sortCriteria);
        List<PupperProfile> pupperProfileList = new ArrayList<>();
        if (puppers.iterator().hasNext()) {
            puppers.forEach(pupperProfileList::add);
        }
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    //TODO: TEST THIS METHOD POST REFACTORING
    public PupperProfileResponse findAllPupperProfilesByUserProfileId(Long userProfileId) {
        List<PupperProfile> pupperProfiles = pupperProfileRepo.findAllByMatchProfile_UserProfile_Id(userProfileId);
        if (pupperProfiles == null || pupperProfiles.isEmpty()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, 
                    String.format(INVALID_OR_EMPTY_USER_PROFILE_ID, userProfileId));
        }
//        Optional<List<MatchProfile>> matchProfileResults = matchProfileRepo.findAllByUserProfile_Id(userProfileId);
//        if (!matchProfileResults.isPresent() || matchProfileResults.get().isEmpty()) {
//            LOGGER.error(ID_NOT_FOUND.replace("id", "userProfile id"), PUPPER_PROFILE, userProfileId);
//            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND,
//                    String.format(ID_NOT_FOUND.replace("id", "userProfile id"), PUPPER_PROFILE, userProfileId));
//        }
//
//        Long matchProfileId = matchProfileResults.get().get(0).getId();
//        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByMatchProfileId(matchProfileId);
//        if (!results.isPresent() || results.get().isEmpty()) {
//            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND,
//                    String.format(INVALID_OR_EMPTY_USER_PROFILE_ID, userProfileId));
//        }

        return createPupperProfileResponse(true, pupperProfiles, HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse findPupperProfileById(Long userId, Long matchProfileId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(ID_NOT_FOUND, PUPPER_PROFILE, pupperId));
        }
        else if (!userId.equals(result.get().getMatchProfile().getUserProfile().getId()) ||
                !matchProfileId.equals(result.get().getMatchProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, Arrays.asList(result.get()),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse createOrUpdatePupperProfileForMatchProfile(Long userId, Long matchId, PupperProfile pupperProfile) {
        if (!userId.equals(pupperProfile.getMatchProfile().getUserProfile().getId()) ||
                !matchId.equals(pupperProfile.getMatchProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        if (StringUtils.isNullOrEmpty(pupperProfile.getLifeStage().value())) {
            pupperProfile.setLifeStage(dobToLifeStage(pupperProfile.getBirthdate()));
        }

        Optional<PupperProfile> result =
                pupperProfileRepo.findByMatchProfileIdAndName(pupperProfile.getMatchProfile().getId(), pupperProfile.getName());

        if (result.isPresent()) {
            LOGGER.info("Pupper profile with that name exists for the given matchProfile.");
            pupperProfile.setId(result.get().getId());
            pupperProfileRepo.save(pupperProfile);

            return createPupperProfileResponse(true, Arrays.asList(result.get()), HttpStatus.OK,
                    DEFAULT_DESCRIPTION);
        }

        PupperProfile profile = pupperProfileRepo.save(pupperProfile);
        return createPupperProfileResponse(true, Arrays.asList(profile), HttpStatus.OK,
                DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse updatePupperProfile(Long userId, Long pupId, final PupperProfile pupperProfile) {
        if (!pupperProfile.getId().equals(pupId) ||
                !pupperProfile.getMatchProfile().getUserProfile().getId().equals(userId)) {
            LOGGER.error(IDS_MISMATCH);
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        if (StringUtils.isNullOrEmpty(pupperProfile.getLifeStage().value())) {
            pupperProfile.setLifeStage(dobToLifeStage(pupperProfile.getBirthdate()));
        }
        PupperProfile profile = pupperProfileRepo.save(pupperProfile);

        return createPupperProfileResponse(true, Arrays.asList(profile), HttpStatus.OK,
                DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse deletePupperProfileById(Long userId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent()) {
            LOGGER.error(ID_NOT_FOUND, PUPPER_PROFILE, pupperId);
          return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        else if (!userId.equals(result.get().getMatchProfile().getUserProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        pupperProfileRepo.deleteById(pupperId);
        return createPupperProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesByBreedId(Long breedId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByBreedId(breedId);
        if (!results.isPresent()) {
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesByMatchProfileId(Long userId, Long matchProfileId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByMatchProfileId(matchProfileId);
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);
        if (!results.isPresent() || results.get().isEmpty()) {
            LOGGER.error(String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));

            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));
        }
        else if (matchProfile.isPresent() && !matchProfile.get().getUserProfile().getId().equals(userId)) {
            LOGGER.error(IDS_MISMATCH);
            return createPupperProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public BreedResponse getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);
            return createBreedResponse(true, breedList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return createBreedResponse(false, null, HttpStatus.NOT_FOUND, NO_QUERY_RESULTS);
    }

}