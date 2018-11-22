package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserAccountRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static java.util.Collections.emptyList;


@Named
@Singleton
public class UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepo userProfileRepo;
    private final UserAccountRepo userAccountRepo;

    @Autowired
    UserProfileService(UserProfileRepo userProfileRepo, UserAccountRepo userAccountRepo) {
        this.userProfileRepo = userProfileRepo;
        this.userAccountRepo = userAccountRepo;
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
        if (user.isPresent()) {
            UserProfile userProfile = user.get();
            userProfile.getUserAccount().setPassword(null);
            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(user.get())),
                    HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        LOGGER.error("User profile with id={} not found", id);

        return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND, id));
    }

    public UserProfileResponse findUserProfileByUserAccountEmail(String email) {
        UserAccount userAccount = userAccountRepo.findByUsername(email);
        Optional<UserProfile> userProfile = userProfileRepo.findByUserAccount(userAccount);
        if (userProfile.isPresent()) {
            userProfile.get().getUserAccount().setPassword(null);
            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(userProfile.get())), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User profile with email={} not found", email);

        return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                String.format("User profile with email=%s not found", email));
    }


        public UserProfileResponse createNewUserProfile(final UserProfile userProfile) {
        Optional<UserProfile> result =
                userProfileRepo.findByFirstNameAndLastNameAndBirthdate(
                        userProfile.getFirstName(),
                        userProfile.getLastName(),
                        userProfile.getBirthdate()); //Generally First Name/Last Name/DOB combo is unique
        if (result.isPresent()) {
            return createUserProfileResponse(false, emptyList(), HttpStatus.CONFLICT,
                    ("CreateNewUserProfile request failed: a userProfile with data matching the request already exists."));

        }
        UserProfile profile = userProfileRepo.save(userProfile);
        profile.getUserAccount().setPassword(null); //Hide password in response

        return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfile(Long userId, final UserProfile userProfile) {
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (!result.isPresent()) {
            LOGGER.error("Error updating user profile for userId {}: invalid request", userId);

            return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format("UpdateUserProfile error: %s", NOT_FOUND));
        }
        else if (!userId.equals(userProfile.getId())) {
            return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        userProfileRepo.save(userProfile);
        userProfile.getUserAccount().setPassword(null); //Hide password in response

        LOGGER.info("User profile with id {} was found, updating existing profile", userId);
        return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(userProfile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse deleteUserProfileById(Long id) {
        Optional<UserProfile> result = userProfileRepo.findById(id);
        if (!result.isPresent()) {
            LOGGER.error("Error deleting user profile: user profile with id {} not found", id);
            return createUserProfileResponse( false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(USER_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id {} was deleted successfully", id);

        return createUserProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    private boolean userProfileFailsNullPointerCheck(UserProfile userProfile) {
        return userProfile == null ||
                userProfile.getId() == null ||
                userProfile.getUserAccount() == null;
    }

}
