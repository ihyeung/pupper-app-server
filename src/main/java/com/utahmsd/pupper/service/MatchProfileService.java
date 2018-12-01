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
import static com.utahmsd.pupper.util.Constants.*;

@Named
@Singleton
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private final String EMPTY_MATCH_PROFILE_LIST = "No match profiles belonging to user profile id %d were found.";

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
        LOGGER.info(NO_QUERY_RESULTS);
        return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, NO_QUERY_RESULTS);

    }

    public MatchProfileResponse getMatchProfileById(Long userId, Long matchProfileId) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !userId.equals(result.get().getUserProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(result.get())), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getAllMatchProfilesByUserId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMPTY_MATCH_PROFILE_LIST, userId));
        }

        return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse createOrUpdateMatchProfileForUser(Long userId, MatchProfile matchProfile) {
        if (!userId.equals(matchProfile.getUserProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        MatchProfile result = matchProfileRepo.findByNamesEquals(matchProfile.getNames());
        if (result != null) {
            LOGGER.info("Match profile for user exists.");
            matchProfile.setScore(result.getScore()); //Reference existing score to prevent it from getting reset
            matchProfile.setId(result.getId()); //Reference existing id
            MatchProfile savedResult = matchProfileRepo.save(matchProfile);
            return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(savedResult)), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        matchProfile.setScore(DEFAULT_SCORE);
        MatchProfile profile = matchProfileRepo.save(matchProfile);
        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchProfileResponse updateMatchProfile(Long userId, Long matchProfileId, MatchProfile matchProfile) {
        if (!matchProfile.getId().equals(matchProfileId) || !matchProfile.getUserProfile().getId().equals(userId)) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_PATH_VARIABLE);
        }
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format("UpdateMatchProfile Error: A matchProfile with userProfileId=%d and matchProfileId=%d " +
                            "does not exist and cannot be updated.", userId, matchProfileId));
        }
        matchProfile.setId(result.get().getId());
        matchProfile.setScore(result.get().getScore()); //Don't allow the user to update their own score
        MatchProfile savedResult = matchProfileRepo.save(matchProfile);
        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(savedResult)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchProfileResponse updateProfileImageByMatchProfileId(Long userId, Long matchProfileId, String imageUrl) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !result.get().getUserProfile().getId().equals(userId)) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_PATH_VARIABLE);
        }
        result.get().setProfileImage(imageUrl); //Update image_url
        MatchProfile savedResult = matchProfileRepo.save(result.get());

        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(savedResult)),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
    }


    public MatchProfileResponse deleteMatchProfile(Long userId, Long matchProfileId) {
        Optional<MatchProfile> matchProfile = matchProfileRepo.findByUserProfileIdAndId(userId, matchProfileId);
        if (!matchProfile.isPresent()) {
            LOGGER.error("Error deleting matchProfile with userProfileId={} and matchProfileId={}", userId, matchProfileId);

            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        matchProfileRepo.delete(matchProfile.get());
        return createMatchProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchProfileResponse deleteMatchProfilesByUserProfileId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfiles.isPresent()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        matchProfileRepo.deleteMatchProfilesByUserProfile_Id(userId);

        return createMatchProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
