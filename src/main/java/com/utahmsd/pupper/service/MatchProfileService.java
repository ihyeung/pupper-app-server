package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.MatchProfileResponse.createMatchProfileResponse;

@Named
@Singleton
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed_id";
    private static final float DEFAULT_SCORE = Float.MAX_VALUE;
    private final String INVALID_USER_ID = "No user profile with user profile id %s exists.";
    private final String EMPTY_MATCH_PROFILE_LIST = "No match profiles belonging to user profile id %s were found.";
    private final String INVALID_MATCH_PROFILE_ID = "No match profile with match profile id %s exists";
    private final String INVALID_REQUEST = "Invalid request.";

    private MatchProfileRepo matchProfileRepo;

    private UserProfileRepo userProfileRepo;

    private PupperProfileRepo pupperProfileRepo;


    public MatchProfileResponse getAllMatchProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        List<MatchProfile> matchProfileList = new ArrayList<>();
        Iterable<MatchProfile> matchProfiles = matchProfileRepo.findAll(sortCriteria);
        if (matchProfiles.iterator().hasNext()) {
            matchProfiles.forEach(matchProfileList::add);
        }

        return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK);

    }

    public MatchProfileResponse getMatchProfilesByUser(Long userId) {
        Optional<UserProfile> userProfile = userProfileRepo.findById(userId);
        if (userProfile.isPresent()) {
            Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile(userProfile.get());
            if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
                MatchProfileResponse response =
                        createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.NO_CONTENT);
                response.setDescription(String.format(EMPTY_MATCH_PROFILE_LIST, userId));
                return response;
            }
            return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK);

        }
        MatchProfileResponse response =
                createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.NOT_FOUND);
        response.setDescription(String.format(INVALID_USER_ID));

        return response;
    }
}
