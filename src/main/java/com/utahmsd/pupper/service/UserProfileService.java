package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;


@Named
@Singleton
public class UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepo userProfileRepo;

    @Autowired
    UserProfileService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }

    public UserProfileResponse getAllUserProfiles() {
        String DEFAULT_SORT_BY_CRITERIA = "lastName";
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_BY_CRITERIA));
        Iterable<UserProfile> users = userProfileRepo.findAll(sortCriteria);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (users.iterator().hasNext()) {
            users.forEach(userProfile -> {
                userProfile.getUserAccount().setPassword(null);
                userProfileList.add(userProfile);
            });
        }

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse findUserProfileById(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        List<UserProfile> userProfileList = new ArrayList<>();
        if (user.isPresent()) {
            userProfileList.add(user.get());
            return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User profile with id {} not found", id);

        return createUserProfileResponse(false, userProfileList, HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND, id));
    }

    public UserProfileResponse createNewUserProfile(UserProfile userProfile) {
        Optional<UserProfile> result = userProfileRepo.findByFirstNameAndLastNameAndBirthdate(userProfile.getFirstName(),
                                                                    userProfile.getLastName(), userProfile.getBirthdate()); //Generally First Name/Last Name/DOB combo is unique
        if (result.isPresent()) {
            return createUserProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST,
                    ("CreateNewUserProfile request failed: a user with data matching the request already exists."));

        }
        UserProfile profile = userProfileRepo.save(userProfile);
        profile.getUserAccount().setPassword(null); //Hide password in response
        List<UserProfile> userProfileList = new ArrayList<>(Arrays.asList(profile));

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfile(Long userId, UserProfile userProfile) {
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (!result.isPresent() || !userId.equals(userProfile.getId())) {
            LOGGER.error("Error updating user profile for userId {}: invalid request", userId);
            return createUserProfileResponse(false, Collections.emptyList(), HttpStatus.BAD_REQUEST,
                    String.format("UpdateUserProfile error: %s", INVALID_REQUEST));
        }
        userProfileRepo.save(userProfile);
        userProfile.getUserAccount().setPassword(null); //Hide password in response
        List<UserProfile> userProfileList = new ArrayList<>(Arrays.asList(userProfile));
        LOGGER.info("User profile with id {} was found, updating existing profile", userId);

        return createUserProfileResponse(true, userProfileList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse deleteUserProfileById(Long id) {
        Optional<UserProfile> result = userProfileRepo.findById(id);
        if (!result.isPresent()) {
            LOGGER.error("Error deleting user profile: user profile with id {} not found", id);
            return createUserProfileResponse( false, Collections.emptyList(), HttpStatus.BAD_REQUEST,  String.format(USER_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id {} was deleted successfully", id);

        return createUserProfileResponse(true, Collections.emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);

    }

}
