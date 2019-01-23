package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.MatchResultRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import com.utahmsd.pupper.dto.pupper.Size;
import com.utahmsd.pupper.util.ProfileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.MatchProfileResponse.createMatchProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;

@Service
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private final String EMPTY_MATCH_PROFILE_LIST = "No match profiles belonging to user profile id %d were found.";

    private final MatchProfileRepo matchProfileRepo;
    private final MatchResultRepo matchResultRepo;
    private  final UserProfileRepo userProfileRepo;

    @Autowired
    public MatchProfileService(MatchProfileRepo matchProfileRepo, MatchResultRepo matchResultRepo,
                               UserProfileRepo userProfileRepo) {
        this.matchProfileRepo = matchProfileRepo;
        this.matchResultRepo = matchResultRepo;
        this.userProfileRepo = userProfileRepo;
    }

    public MatchProfileResponse getAllMatchProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        List<MatchProfile> matchProfileList = new ArrayList<>();
        Iterable<MatchProfile> matchProfiles = matchProfileRepo.findAll(sortCriteria);
        if (matchProfiles.iterator().hasNext()) {
            matchProfiles.forEach(each -> {
                each.setScore(null);
                matchProfileList.add(each);
            });
            return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        LOGGER.info(NO_QUERY_RESULTS);
        return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, NO_QUERY_RESULTS);

    }
    public MatchProfile getMatchProfileByMatchProfileId(Long matchProfileId) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        return result.orElse(null);
    }

        public MatchProfileResponse getMatchProfileByIds(Long userId, Long matchProfileId) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !userId.equals(result.get().getUserProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        result.get().setScore(null);
        return createMatchProfileResponse(true, Arrays.asList(result.get()), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getAllMatchProfilesByUserId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMPTY_MATCH_PROFILE_LIST, userId));
        }
        matchProfiles.get().forEach(each -> each.setScore(null));
        return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse createMatchProfileForUser(Long userId, MatchProfile matchProfile) {
        if (!userId.equals(matchProfile.getUserProfile().getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        Optional<UserProfile> userProfile = userProfileRepo.findById(userId);
        if (!userProfile.isPresent()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(USER_PROFILE_NOT_FOUND, userId));
        }
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfiles.isPresent()) {
            return initEmptyMatchProfileFields(matchProfile, true);
        }
        for (MatchProfile profile: matchProfiles.get()) {
            if (profile.getNames().equals(matchProfile.getNames()) &&
                    profile.getBreed().getName().equals(matchProfile.getBreed().getName())) { //Trying to create a match profile with name/breed matching an existing match profile
                return createMatchProfileResponse(false, null, HttpStatus.BAD_REQUEST,
                        "Creating duplicate match profile for user with names and breed matching an existing match profile");
            }
            if (matchProfile.getDefault()) {
                updateIsDefaultForMatchProfile(profile);
            }
        }
        return initEmptyMatchProfileFields(matchProfile, null);
    }

    private void updateIsDefaultForMatchProfile(MatchProfile matchProfileToUpdate) {
        if (matchProfileToUpdate.getDefault()) { //Another match profile is current default, need to update
            matchProfileToUpdate.setDefault(false);
            matchProfileRepo.save(matchProfileToUpdate);
        }
    }

    private MatchProfileResponse initEmptyMatchProfileFields(MatchProfile matchProfile, Boolean makeDefault) {
        matchProfile.setScore(DEFAULT_MAX_SCORE);

        if (matchProfile.getSize() == null) {
            Size sizeFromBreed = matchProfile.getBreed().getSize() == null ? Size.UNKNOWN : matchProfile.getBreed().getSize();
            matchProfile.setSize(sizeFromBreed);
        }
        if (matchProfile.getLifeStage() == null) {
            LifeStage lifeStage = ProfileUtils.dobToLifeStage(matchProfile.getBirthdate());
            matchProfile.setLifeStage(lifeStage);
        }
        if (makeDefault != null) {
            matchProfile.setDefault(makeDefault); //Override makeDefault flag if creating first match profile for user
        }
        MatchProfile profile = matchProfileRepo.save(matchProfile);
        profile.setScore(null); //Hide score in response
        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    private MatchProfileResponse setMatchProfileFieldsWithQueryResultThenSave(MatchProfile matchProfileForUpdate,
                                                                              MatchProfile matchProfileQueryResult) {
        matchProfileForUpdate.setScore(matchProfileQueryResult.getScore()); //Reference existing score to prevent it from getting reset
        matchProfileForUpdate.setId(matchProfileQueryResult.getId()); //Reference existing id
        MatchProfile savedResult = matchProfileRepo.save(matchProfileForUpdate);
        savedResult.setScore(null); //Hide score in response
        return createMatchProfileResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }


    public MatchProfileResponse updateMatchProfileByUserIdAndMatchProfileId(Long userId, Long matchProfileId, MatchProfile matchProfile) {
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
        return setMatchProfileFieldsWithQueryResultThenSave(matchProfile, result.get());
    }

    public MatchProfileResponse updateProfileImageByMatchProfileId(Long userId, Long matchProfileId, String imageUrl) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !result.get().getUserProfile().getId().equals(userId)) {
            LOGGER.error(IDS_MISMATCH);
            return createMatchProfileResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_PATH_VARIABLE);
        }
        result.get().setProfileImage(imageUrl); //Update image_url
        MatchProfile savedResult = matchProfileRepo.save(result.get());
        savedResult.setScore(null);
        return createMatchProfileResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }


    public void deleteMatchProfile(Long userId, Long matchProfileId) {
        Optional<MatchProfile> matchProfile = matchProfileRepo.findByUserProfileIdAndId(userId, matchProfileId);
        if (!matchProfile.isPresent()) {
            LOGGER.error("Error deleting matchProfile with userProfileId={} and matchProfileId={}", userId, matchProfileId);
        } else {
            matchProfileRepo.delete(matchProfile.get());
        }
    }

    public MatchProfileResponse deleteMatchProfilesByUserProfileId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile_Id(userId);
        if (!matchProfiles.isPresent()) {
            return createMatchProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        matchProfileRepo.deleteMatchProfilesByUserProfile_Id(userId);

        return createMatchProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public List<MatchProfile> retrieveMatchesForMatchProfile(Long matchProfileId) {
        List<Integer> matchesIdList = matchResultRepo.retrieveIdsOfAllMutualMatches(matchProfileId, matchProfileId);
        //Cast ids from result set from Integers to Longs in order to make repo call to retrieve match profiles
        List<Long> matchProfileIds = new ArrayList<>();
        matchesIdList.forEach(id -> matchProfileIds.add(id.longValue()));

        List<MatchProfile> mutualMatches = matchProfileRepo.findAllById(matchProfileIds);

        return new ArrayList<>(mutualMatches);
    }
}
