package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import com.utahmsd.pupper.dto.PupperProfileRequest;
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

import static com.utahmsd.pupper.dto.PupperProfileResponse.*;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_REQUEST;

@Named
@Singleton
public class PupperProfileService {

    private final String DEFAULT_SORT_BY_CRITERIA = "breed";

    private final String INVALID_USER_ID = "No user profile with user profile id %s exists.";
    private final String INVALID_PUPPER_ID = "No pupper profile with profile id %s exists";
    private final String EMPTY_PUPPER_LIST_FOR_USER = "No pupper profiles belonging to user profile id %s were found.";
    private final String EMPTY_PUPPER_LIST_FOR_MATCH_PROFILE_AND_USER_ID =
            "No pupper profiles belonging to user profile id %s and match profile %s were found.";


    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;

    @Inject
    BreedRepo breedRepo;

    public PupperProfileResponse getAllPupperProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<PupperProfile> puppers = pupperProfileRepo.findAll(sortCriteria);
        List<PupperProfile> pupperProfileList = new ArrayList<>();
        if (puppers.iterator().hasNext()) {
            puppers.forEach(pupperProfileList::add);
        }
        return createPupperProfileResponse(true, pupperProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse findAllPupperProfilesByUserId(Long userId) {
        Optional<UserProfile> user = userProfileRepo.findById(userId);
        if (!user.isPresent()) {
            PupperProfileResponse response = createPupperProfileResponse(
                    false, Collections.emptyList(), HttpStatus.BAD_REQUEST, String.format(INVALID_USER_ID, userId));

            return response;
        }
        Optional<List<PupperProfile>> pupperProfileList = pupperProfileRepo.findAllByUserProfile(user.get());
        if (!pupperProfileList.isPresent() || pupperProfileList.get().isEmpty()) {
            PupperProfileResponse response =
                    createPupperProfileResponse(false, Collections.emptyList(), HttpStatus.NO_CONTENT, String.format(EMPTY_PUPPER_LIST_FOR_USER, userId));
            return response;
        }
        return createPupperProfileResponse(true, pupperProfileList.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse findPupperProfile(Long userId, Long pupperId) {
        PupperProfileResponse response = findAllPupperProfilesByUserId(userId);
        List<PupperProfile> pupperProfiles = response.getPupperProfileList();
        if (pupperProfiles == null || pupperProfiles.isEmpty()) {
            return  response;
        }
        List<PupperProfile> pupperProfile = new ArrayList<>();
        for (PupperProfile p : pupperProfiles) {
            if (p.getId() == pupperId) {
                pupperProfile.add(p);
                return createPupperProfileResponse(true, pupperProfile, HttpStatus.OK, DEFAULT_DESCRIPTION);
            }
        }
        PupperProfileResponse invalidPupperIdResponse =
                createPupperProfileResponse(false, pupperProfile, HttpStatus.NO_CONTENT, String.format(INVALID_PUPPER_ID, pupperId));
        return invalidPupperIdResponse;
    }

    public PupperProfileResponse createNewPupperProfile(PupperProfileRequest request) {
        PupperProfile profile = pupperProfileRepo.save(request.getPupperProfile());
        List<PupperProfile> pupperProfile = new ArrayList<>();
        pupperProfile.add(profile);
        return createPupperProfileResponse(true, pupperProfile, HttpStatus.CREATED, DEFAULT_DESCRIPTION);
    }

    public PupperProfileResponse updatePupperProfile(Long userId, Long pupId, PupperProfileRequest request) {
        if (!validateUserIdAndPupperIdMatchesRequest(userId, pupId, request)) {
            PupperProfileResponse invalidRequestResponse =
                    createPupperProfileResponse(false, Collections.emptyList(), HttpStatus.UNAUTHORIZED, INVALID_REQUEST);
            return invalidRequestResponse;
        }
        PupperProfile profile = pupperProfileRepo.save(request.getPupperProfile());
        List<PupperProfile> pupperProfile = new ArrayList<>();
        pupperProfile.add(profile);
        return createPupperProfileResponse(true, pupperProfile, HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public PupperProfileResponse deletePupperProfile(Long userId, Long pupperId) {
        PupperProfileResponse response = findPupperProfile(userId, pupperId);
        if (response.getPupperProfileList() != null && !response.getPupperProfileList().isEmpty()) {
            PupperProfile profileToDelete = response.getPupperProfileList().get(0);
            pupperProfileRepo.deleteById(profileToDelete.getId());
            List<PupperProfile> deletedProfile = new ArrayList<>();
            deletedProfile.add(profileToDelete);
            return createPupperProfileResponse(true, deletedProfile, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return createPupperProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
    }

    private boolean validateUserIdAndPupperIdMatchesRequest(Long userId, Long pupId, PupperProfileRequest request) {
        if (request == null || request.getPupperProfile() == null) {
            return false;
        }
        PupperProfile pupperProfile = request.getPupperProfile();
        if (pupperProfile.getId() == null || pupperProfile.getUserProfile() == null ||
                pupperProfile.getUserProfile().getId() == null) {
            return false;
        }
        return pupperProfile.getId() == pupId && pupperProfile.getUserProfile().getId() == userId;
    }



    public PupperProfileResponse getBreeds() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "breed"));
        Iterable<Breed> breeds = breedRepo.findAll(sortCriteria);
        List<Breed> breedList = new ArrayList<>();
        if (breeds.iterator().hasNext()) {
            breeds.forEach(breedList::add);
        }
        breedList.forEach(e->{
            System.out.println(e.getBreed());
        });

        return createPupperProfileResponse(true, Collections.emptyList(), HttpStatus.OK, breedList.toString());
    }

    public PupperProfileResponse getAllPuppersInMatchProfile(Long userId, Long matchProfileId) {
        PupperProfileResponse response = findAllPupperProfilesByUserId(userId);
        if (!response.isSuccess() || response.getPupperProfileList() == null || response.getPupperProfileList().isEmpty()) {
            LOGGER.error(EMPTY_PUPPER_LIST_FOR_USER, userId);

            return createPupperProfileResponse(
                    false, Collections.emptyList(), HttpStatus.BAD_REQUEST, String.format(EMPTY_PUPPER_LIST_FOR_USER, userId));
        }
        List<PupperProfile> puppersWithMatchProfileId = new ArrayList<>();
        response.getPupperProfileList().forEach(pup -> {
            if (pup.getMatchProfile() != null && pup.getMatchProfile().getId() == matchProfileId) {
                puppersWithMatchProfileId.add(pup);
            }
        });

        if (!puppersWithMatchProfileId.isEmpty()) {
            return createPupperProfileResponse(true, puppersWithMatchProfileId, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        LOGGER.error(EMPTY_PUPPER_LIST_FOR_MATCH_PROFILE_AND_USER_ID, matchProfileId, userId);

        return createPupperProfileResponse(false,
                puppersWithMatchProfileId,
                HttpStatus.BAD_REQUEST,
                String.format(EMPTY_PUPPER_LIST_FOR_MATCH_PROFILE_AND_USER_ID, matchProfileId, userId));
    }

}