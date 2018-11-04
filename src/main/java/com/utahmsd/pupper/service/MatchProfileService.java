package com.utahmsd.pupper.service;

import com.utahmsd.pupper.client.AmazonAwsClient;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.MatchProfileRequest;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.MatchProfileResponse.createMatchProfileResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;

@Named
@Singleton
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed";
    private static final float DEFAULT_SCORE = Float.MAX_VALUE;
    private final String INVALID_USER_ID = "No user profile with user profile id %s exists.";
    private final String EMPTY_MATCH_PROFILE_LIST = "No match profiles belonging to user profile id %s were found.";
    private final String INVALID_MATCH_PROFILE_ID = "No match profile with match profile id %s exists";

    private final MatchProfileRepo matchProfileRepo;
    private final UserProfileRepo userProfileRepo;
    private final PupperProfileRepo pupperProfileRepo;
    private final AmazonAwsClient amazonAwsClient;

    @Autowired
    public MatchProfileService(UserProfileRepo userProfileRepo, PupperProfileRepo pupperProfileRepo,
                               MatchProfileRepo matchProfileRepo, AmazonAwsClient amazonAwsClient) {
        this.matchProfileRepo = matchProfileRepo;
        this.pupperProfileRepo = pupperProfileRepo;
        this.userProfileRepo = userProfileRepo;
        this.amazonAwsClient = amazonAwsClient;
    }

    public MatchProfileResponse getAllMatchProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        List<MatchProfile> matchProfileList = new ArrayList<>();
        Iterable<MatchProfile> matchProfiles = matchProfileRepo.findAll(sortCriteria);
        if (matchProfiles.iterator().hasNext()) {
            matchProfiles.forEach(matchProfileList::add);
            return createMatchProfileResponse(true, matchProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        LOGGER.info("No match profiles were found.");
        return createMatchProfileResponse(true, matchProfileList, HttpStatus.NO_CONTENT, DEFAULT_DESCRIPTION);

    }

    public MatchProfileResponse getMatchProfilesByUser(Long userId) {
        Optional<UserProfile> userProfile = userProfileRepo.findById(userId);
        if (userProfile.isPresent()) {
            Optional<List<MatchProfile>> matchProfiles = matchProfileRepo.findAllByUserProfile(userProfile.get());
            if (!matchProfiles.isPresent() || matchProfiles.get().isEmpty()) {
                return createMatchProfileResponse(false,
                        Collections.emptyList(),
                        HttpStatus.NO_CONTENT,
                        String.format(EMPTY_MATCH_PROFILE_LIST, userId));
            }
            return createMatchProfileResponse(true, matchProfiles.get(), HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        return createMatchProfileResponse(false, Collections.emptyList(), HttpStatus.NOT_FOUND, String.format(INVALID_USER_ID));

    }

    public MatchProfileResponse createMatchProfileForUser(Long userId, MatchProfileRequest request) {
        return null;
    }

    public MatchProfileResponse updateMatchprofile(MatchProfileRequest request) {
        return null;
    }

    public void addPhotoToMatchProfile(Long userId, MatchProfileRequest request) {
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
