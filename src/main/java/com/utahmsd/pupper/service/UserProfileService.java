package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.dto.UserProfileRequest;
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

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.USER_NOT_FOUND;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;


@Named
@Singleton
public class UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    UserProfileRepo userProfileRepo;

    public UserProfileResponse getAllUserProfiles() {
        String DEFAULT_SORT_BY_CRITERIA = "lastName";
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<UserProfile> users = userProfileRepo.findAll(sortCriteria);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (users.iterator().hasNext()) {
            users.forEach(userProfileList::add);
        }

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse findUserProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (user.isPresent()) {
            userProfileList.add(user.get());
            return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User profile with id {} not found", id);

        return createUserProfileResponse(false, userProfileList, HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND, id));
    }

    public UserProfileResponse createNewUserProfile(UserProfileRequest request) {
        UserProfile profile = userProfileRepo.save(request.getUserProfile());
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(profile);

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfile(UserProfileRequest request) {
        final Long userId = request.getUserProfile().getId();
        UserProfileResponse userProfileResponse = findUserProfile(userId);
        if (!userProfileResponse.isSuccess()) {
            LOGGER.error("Error updating user profile: user profile with id {} was not found", userId);
            return createUserProfileResponse(false, Collections.emptyList(), HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND, userId));
        }
        userProfileRepo.save(request.getUserProfile());
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(request.getUserProfile());
        LOGGER.error("User profile with id {} was found, updating existing profile", userId);

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse deleteProfile(Long id) {
        UserProfileResponse userProfileResponse = findUserProfile(id);
        if (!userProfileResponse.isSuccess()) {
            LOGGER.error("Error deleting user profile: user profile with id {} not found", id);
            return createUserProfileResponse( false, Collections.emptyList(), HttpStatus.NOT_FOUND,  String.format(USER_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.error("User profile with id {} was deleted successfully", id);

        return createUserProfileResponse(true, Collections.emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

}
