package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileRequest;
import com.utahmsd.pupper.dto.UserProfileResponse;
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

@Named
@Singleton
public class UserProfileService {

    private final String DEFAULT_SORT_BY_CRITERIA = "lastName";

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    UserProfileRepo userProfileRepo;

    public UserProfileResponse getAllUserProfiles() {
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<UserProfile> users = userProfileRepo.findAll(sortCriteria);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (users.iterator().hasNext()) {
            users.forEach(userProfileList::add);
        }

        return UserProfileResponse.createUserProfileResponse(true, userProfileList, HttpStatus.OK);
    }

    public UserProfileResponse findUserProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (user.isPresent()) {
            userProfileList.add(user.get());
            return UserProfileResponse.createUserProfileResponse(true, userProfileList, HttpStatus.OK);
        }
        LOGGER.info("User profile with id {} not found", id);

        return UserProfileResponse.createUserProfileResponse(false, userProfileList, HttpStatus.NOT_FOUND);
    }

    public UserProfileResponse createNewUserProfile(UserProfileRequest request) {
        UserProfile profile = userProfileRepo.save(request.getUserProfile());
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(profile);

        return UserProfileResponse.createUserProfileResponse(true, userProfileList, HttpStatus.OK);
    }

    public UserProfileResponse updateUserProfile(UserProfileRequest request) {
        UserProfileResponse userProfileResponse = findUserProfile(request.getUserProfile().getId());
        if (!userProfileResponse.isSuccess()) {
            LOGGER.info("Error updating user profile: user profile with id {} was not found", request.getUserProfile().getId());
            return UserProfileResponse.createUserProfileResponse(false,
                    Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        userProfileRepo.save(request.getUserProfile());
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(request.getUserProfile());
        LOGGER.info("User profile with id {} was found, updating existing profile", request.getUserProfile().getId());

        return UserProfileResponse.createUserProfileResponse(true, userProfileList, HttpStatus.OK);
    }

    public UserProfileResponse deleteProfile(Long id) {
        UserProfileResponse userProfileResponse = findUserProfile(id);
        if (!userProfileResponse.isSuccess()) {
            LOGGER.info("Error deleting user profile: user profile with id {} not found", id);
            return UserProfileResponse.createUserProfileResponse( false, Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id {} was deleted successfully", id);

        return UserProfileResponse.createUserProfileResponse(true, Collections.emptyList(), HttpStatus.OK);

    }

}
