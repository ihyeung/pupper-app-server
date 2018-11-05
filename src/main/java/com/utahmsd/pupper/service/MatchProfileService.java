package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.utahmsd.pupper.dto.MatchProfileResponse.createMatchProfileResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_REQUEST;

@Named
@Singleton
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private static final float DEFAULT_SCORE = Float.MAX_VALUE;
    private final String INVALID_USER_ID = "No user profile with user profile id %s exists.";
    private final String EMPTY_MATCH_PROFILE_LIST = "No match profiles belonging to user profile id %d were found.";
    private final String INVALID_MATCH_PROFILE_ID = "No match profile with match profile id %s exists";

    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    public MatchProfileService(MatchProfileRepo matchProfileRepo) {
        this.matchProfileRepo = matchProfileRepo;
    }

    public MatchProfileResponse getAllMatchProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        List<MatchProfile> matchProfileList = new ArrayList<>();
        Iterable<MatchProfile> matchProfiles = matchProfileRepo.findAll(sortCriteria);
        if (matchProfiles.iterator().hasNext()) {
            matchProfiles.forEach(matchProfileList::add);
            return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        LOGGER.info("No match profiles were found in the match_profile database table");
        return createMatchProfileResponse(true, matchProfileList, HttpStatus.NO_CONTENT, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getMatchProfileById(Long userId, Long matchProfileId) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !userId.equals(result.get().getUserProfile().getId())) {
            LOGGER.error("Invalid getMatchProfileById request");

            return createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        List<MatchProfile> profiles = new ArrayList<>(Arrays.asList(result.get()));
        return createMatchProfileResponse(true, profiles, HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getAllMatchProfilesByUserId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfileId(userId);
        if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
            return createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(EMPTY_MATCH_PROFILE_LIST, userId));
        }
        return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse createMatchProfileForUser(Long userId, MatchProfile matchProfile) {
        Optional<MatchProfile> result = matchProfileRepo.findByUserProfileIdAndBreedAndEnergyLevelAndLifeStage(
                matchProfile.getUserProfile().getId(), matchProfile.getBreed(), matchProfile.getEnergyLevel(), matchProfile.getLifeStage());
        if (result.isPresent() || !userId.equals(matchProfile.getUserProfile().getId())) {
            LOGGER.error("Invalid createMatchProfileForUser request");
            return createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        MatchProfile match = matchProfileRepo.save(matchProfile);
        List<MatchProfile> matchProfiles = new ArrayList<>(Arrays.asList(match));
        return createMatchProfileResponse(true, matchProfiles, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchProfileResponse updateMatchprofile(Long userId, MatchProfile matchProfile) {
        return null;
    }

    public MatchProfileResponse deleteMatchProfile(Long userId, MatchProfile matchProfile) {
        return null;
    }

    public void addPhotoToMatchProfile(Long userId, MatchProfile matchProfile) {
        //TODO: upload photo to s3
        String profilePhoto = uploadProfilePhoto();
        //Set photo url field in match profile to uploaded filepath
        //Update in database
    }

    private String uploadProfilePhoto() {
        //Upload photo
        //Return filepath/filename
        return null;
    }


}
