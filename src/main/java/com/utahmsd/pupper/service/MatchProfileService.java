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
import static com.utahmsd.pupper.util.Constants.INVALID_PATH_VARIABLE;
import static com.utahmsd.pupper.util.Constants.NOT_FOUND;
import static java.util.Collections.emptyList;

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
            matchProfiles.forEach(matchProfile -> {
                matchProfile.getUserProfile().getUserAccount().setPassword(null);
                matchProfileList.add(matchProfile);
            });
            return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        LOGGER.info("No match profiles were found in the match_profile database table");
        return createMatchProfileResponse(true, matchProfileList, HttpStatus.NO_CONTENT, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getMatchProfileById(Long userId, Long matchProfileId) {
        Optional<MatchProfile> result = matchProfileRepo.findById(matchProfileId);
        if (!result.isPresent() || !userId.equals(result.get().getUserProfile().getId())) {
            LOGGER.error("Invalid getMatchProfileById request");

            return createMatchProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        result.get().getUserProfile().getUserAccount().setPassword(null);
        
        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(result.get())), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getAllMatchProfilesByUserId(Long userId) {
        Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfileId(userId);
        if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
            return createMatchProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(EMPTY_MATCH_PROFILE_LIST, userId));
        }
        matchProfiles.get().forEach(matchProfile -> matchProfile.getUserProfile().getUserAccount().setPassword(null));//Hide password
        return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse createMatchProfileForUser(Long userId, MatchProfile matchProfile) {
        Optional<MatchProfile> result = matchProfileRepo.findByUserProfileIdAndBreedAndEnergyLevelAndLifeStage(
                matchProfile.getUserProfile().getId(), matchProfile.getBreed(), matchProfile.getEnergyLevel(), matchProfile.getLifeStage());
        if (result.isPresent()) {
            return createMatchProfileResponse(false, emptyList(), HttpStatus.CONFLICT,
                    String.format("A matchProfile for userProfileId=%d " +
                    " with data matching the request parameters already exists.", userId));

        }
        else if (!userId.equals(matchProfile.getUserProfile().getId())) {
            return createMatchProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        MatchProfile match = matchProfileRepo.save(matchProfile);
        match.getUserProfile().getUserAccount().setPassword(null);

        return createMatchProfileResponse(true, new ArrayList<>(Arrays.asList(match)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchProfileResponse updateMatchProfile(Long userId, Long matchProfileId, MatchProfile matchProfile) {
        return null;
    }

    public MatchProfileResponse deleteMatchProfile(Long userId, Long matchProfileId) {
        return null;
    }

    public MatchProfileResponse deleteMatchProfilesByUserId(Long userId) {
        return null;
    }
}
