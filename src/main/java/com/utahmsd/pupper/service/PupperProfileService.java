package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.Breed;
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
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_REQUEST;
import static java.util.Collections.emptyList;

@Named
@Singleton
public class PupperProfileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private final String INVALID_USER_ID = "No user profile with user profile id %d exists.";
    private final String INVALID_PUPPER_ID = "No pupper profile with profile id %d exists";
    private final String EMPTY_PUPPER_LIST_FOR_USER = "No pupper profiles belonging to user profile id %d were found.";
    private final String EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID =
            "No pupper profiles belonging to user profile id %d and match profile %d were found.";

    private final PupperProfileRepo pupperProfileRepo;
    private final UserProfileRepo userProfileRepo;
    private final BreedRepo breedRepo;

    @Autowired
    PupperProfileService (PupperProfileRepo pupperProfileRepo, UserProfileRepo userProfileRepo, BreedRepo breedRepo) {
        this.pupperProfileRepo = pupperProfileRepo;
        this.userProfileRepo = userProfileRepo;
        this.breedRepo = breedRepo;
    }

    public PupperProfileResponse getAllPupperProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<PupperProfile> puppers = pupperProfileRepo.findAll(sortCriteria);
        List<PupperProfile> pupperProfileList = new ArrayList<>();
        if (puppers.iterator().hasNext()) {
            puppers.forEach(pupperProfile -> {
                pupperProfile.getUserProfile().getUserAccount().setPassword(null);
                pupperProfileList.add(pupperProfile);
            });
        }
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse findAllPupperProfilesByUserId(Long userId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByUserProfileId(userId);
        if (!results.isPresent() || results.get().isEmpty()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(EMPTY_PUPPER_LIST_FOR_USER, userId));
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse findPupperProfileById(Long userId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent() || !(userId.equals(result.get().getUserProfile().getId()))) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(INVALID_PUPPER_ID, pupperId));
        }
        List<PupperProfile> pupperProfile = new ArrayList<>(Arrays.asList(result.get()));
        result.get().getUserProfile().getUserAccount().setPassword(null);

        return createPupperProfileResponse(true, pupperProfile, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse createNewPupperProfileForMatchProfile(Long userId, Long matchId, PupperProfile pupperProfile) {
        if (!userId.equals(pupperProfile.getUserProfile().getId()) || !matchId.equals(pupperProfile.getMatchProfile().getId())) {
            LOGGER.error("Invalid createNewPupperProfileForMatchProfile request");
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        Optional<PupperProfile> result =
                pupperProfileRepo.findByUserProfileIdAndName(pupperProfile.getUserProfile().getId(), pupperProfile.getName());
        if (result.isPresent()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format("A pupper associated with userProfile %d named '%s' already exists.",
                            pupperProfile.getUserProfile().getId(), pupperProfile.getName()));
        }
        PupperProfile profile = pupperProfileRepo.save(pupperProfile);
        profile.getUserProfile().getUserAccount().setPassword(null);
        List<PupperProfile> pupperProfileList = new ArrayList<>(Arrays.asList(profile));
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse updatePupperProfile(Long userId, Long pupId, PupperProfile pupperProfile) {
        if (!validateUserIdAndPupperIdMatchesRequest(userId, pupId, pupperProfile)) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        PupperProfile profile = pupperProfileRepo.save(pupperProfile);
        profile.getUserProfile().getUserAccount().setPassword(null);

        List<PupperProfile> pupper = new ArrayList<>(Arrays.asList(profile));
        return createPupperProfileResponse(true, pupper, HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse deletePupperProfileById(Long userId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent() || !userId.equals(result.get().getUserProfile().getId())) {
            LOGGER.error("Invalid deletePupperProfileById request");
          return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        pupperProfileRepo.deleteById(pupperId);

        return createPupperProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    private boolean validateUserIdAndPupperIdMatchesRequest(Long userId, Long pupId, PupperProfile pupperProfile) {
        if (pupperProfile == null || pupperProfile.getId() == null || pupperProfile.getUserProfile() == null ||
                pupperProfile.getUserProfile().getId() == null) {
            return false;
        }
        return pupperProfile.getId().equals(pupId) && pupperProfile.getUserProfile().getId().equals(userId);
    }

    public BreedResponse getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);

            return createBreedResponse(true, breedList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        return createBreedResponse(false, emptyList(), HttpStatus.BAD_REQUEST, "No results found.");
    }

    public PupperProfileResponse getAllPuppersByMatchProfileId(Long userId, Long matchProfileId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByUserProfileIdAndMatchProfileId(userId, matchProfileId);
        if (!results.isPresent() || results.get().isEmpty()) {
            LOGGER.error(String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));

            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));
        }
        results.get().forEach(pupperProfile -> pupperProfile.getUserProfile().getUserAccount().setPassword(null)); //Hide passwords
        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

}