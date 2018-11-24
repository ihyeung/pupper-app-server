package com.utahmsd.pupper.service;

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
import static com.utahmsd.pupper.util.PupperUtils.dobToLifeStage;
import static java.util.Collections.emptyList;

@Named
@Singleton
public class PupperProfileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private final String PUPPER_PROFILE = "PupperProfile";
    private final String INVALID_PUPPER_ID = "No pupper profile with profile id %d exists";
    private final String EMPTY_PUPPER_LIST_FOR_USER = "No pupper profiles belonging to user profile id %d were found.";
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
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<PupperProfile> puppers = pupperProfileRepo.findAll(sortCriteria);
        List<PupperProfile> pupperProfileList = new ArrayList<>();
        if (puppers.iterator().hasNext()) {
            puppers.forEach(pupperProfile -> {
                pupperProfile.getMatchProfile().getUserProfile().getUserAccount().setPassword(null);
                pupperProfileList.add(pupperProfile);
            });
        }
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse findAllPupperProfilesByUserId(Long userId) {
        Optional<List<MatchProfile>> matchProfileResults = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfileResults.isPresent() || matchProfileResults.get().isEmpty()) {
            LOGGER.error("No match profiles corresponding to userProfileId={} were found. Therefore, no pupper profiles exist" +
                    "that correspond to this userProfileId.", userId);
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format("No matchProfiles nor pupperProfiles corresponding to userProfileId=%d were found.", userId));
        }

        Long matchProfileId = matchProfileResults.get().get(0).getId();
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByMatchProfileId(matchProfileId);
        if (!results.isPresent() || results.get().isEmpty()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(EMPTY_PUPPER_LIST_FOR_USER, userId));
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse findPupperProfileById(Long userId, Long matchProfileId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(INVALID_PUPPER_ID, pupperId));
        }

        else if (pupperProfileFailsNullPointerCheck(result.get())) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(NULL_FIELD, PUPPER_PROFILE));
        }
        else if (!userId.equals(result.get().getMatchProfile().getUserProfile().getId()) ||
                !matchProfileId.equals(result.get().getMatchProfile().getId())) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, new ArrayList<>(Arrays.asList(result.get())),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse createNewPupperProfileForMatchProfile(Long userId, Long matchId, PupperProfile pupperProfile) {
        if (pupperProfileFailsNullPointerCheck(pupperProfile)) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(NULL_FIELD, PUPPER_PROFILE));
        }
        else if (!userId.equals(pupperProfile.getMatchProfile().getUserProfile().getId()) ||
                !matchId.equals(pupperProfile.getMatchProfile().getId())) {
            LOGGER.error("Invalid createNewPupperProfileForMatchProfile request");

            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        Optional<PupperProfile> result =
                pupperProfileRepo.findByMatchProfileIdAndName(pupperProfile.getMatchProfile().getId(), pupperProfile.getName());

        if (result.isPresent()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.CONFLICT,
                    String.format("A pupperProfile named '%s' associated with userProfileId=%d and matchProfileId=%d already exists.",
                            pupperProfile.getName(), pupperProfile.getMatchProfile().getUserProfile().getId(), pupperProfile.getMatchProfile().getId()));
        }

        if (pupperProfile.getLifeStage() == null) {
            pupperProfile.setLifeStage(dobToLifeStage(pupperProfile.getBirthdate()));
        }

        PupperProfile profile = pupperProfileRepo.save(pupperProfile);
        return createPupperProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK,
                DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse updatePupperProfile(Long userId, Long pupId, PupperProfile pupperProfile) {
        if (pupperProfileFailsNullPointerCheck(pupperProfile)) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(NULL_FIELD, PUPPER_PROFILE));
        }
        else if (!pupperProfile.getId().equals(pupId) ||
                !pupperProfile.getMatchProfile().getUserProfile().getId().equals(userId)) {

            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    INVALID_PATH_VARIABLE);
        }

        if (pupperProfile.getLifeStage() == null) {
            pupperProfile.setLifeStage(dobToLifeStage(pupperProfile.getBirthdate()));
        }

        pupperProfileRepo.save(pupperProfile);

        return createPupperProfileResponse(true, new ArrayList<>(Arrays.asList(pupperProfile)), HttpStatus.OK,
                DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse deletePupperProfileById(Long userId, Long pupperId) {
        Optional<PupperProfile> result = pupperProfileRepo.findById(pupperId);
        if (!result.isPresent()) {
            LOGGER.error("Invalid deletePupperProfileById request");
          return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        else if (pupperProfileFailsNullPointerCheck(result.get())) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(NULL_FIELD, PUPPER_PROFILE));
        }
        else if (!userId.equals(result.get().getMatchProfile().getUserProfile().getId())) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        pupperProfileRepo.deleteById(pupperId);
        return createPupperProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesByBreedId(Long breedId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByBreedId(breedId);
        if (!results.isPresent()) {
            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    INVALID_PATH_VARIABLE);
        }

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse getPupperProfilesByMatchProfileId(Long userId, Long matchProfileId) {
        Optional<List<PupperProfile>> results = pupperProfileRepo.findAllByMatchProfileId(matchProfileId);
        Optional<MatchProfile> matchProfile = matchProfileRepo.findById(matchProfileId);
        if (!results.isPresent() || results.get().isEmpty()) {
            LOGGER.error(String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));

            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(EMPTY_PUPPER_LIST_FOR_USER_ID_AND_MATCH_PROFILE_ID, userId, matchProfileId));
        }
        else if (matchProfile.isPresent() && !matchProfile.get().getUserProfile().getId().equals(userId)) {
            LOGGER.error("The value of userProfileId={} in the endpoint path does not match the userProfileId that " +
                    "matchProfileId={} belongs to.", userId, matchProfileId);

            return createPupperProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        results.get().forEach(pupperProfile ->
                pupperProfile.getMatchProfile().getUserProfile().getUserAccount().setPassword(null)); //Hide passwords

        return createPupperProfileResponse(true, results.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    private boolean pupperProfileFailsNullPointerCheck(PupperProfile pupperProfile) {
        return pupperProfile == null ||
                pupperProfile.getId() == null ||
                pupperProfile.getMatchProfile() == null ||
                pupperProfile.getMatchProfile().getUserProfile() == null ||
                pupperProfile.getMatchProfile().getUserProfile().getId() == null;
    }

    public BreedResponse getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);

            return createBreedResponse(true, breedList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        return createBreedResponse(false, emptyList(), HttpStatus.NOT_FOUND, "No results found.");
    }

}