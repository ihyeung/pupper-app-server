package com.utahmsd.pupper.service;

import com.amazonaws.util.StringUtils;
import com.utahmsd.pupper.dao.UserAccountRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static java.util.Collections.emptyList;


@Named
@Singleton
public class UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final String DEFAULT_SORT_ORDER = "lastName";

    private final UserProfileRepo userProfileRepo;
    private final UserAccountRepo userAccountRepo;

    @Autowired
    UserProfileService(UserProfileRepo userProfileRepo, UserAccountRepo userAccountRepo) {
        this.userProfileRepo = userProfileRepo;
        this.userAccountRepo = userAccountRepo;
    }

    public UserProfileResponse getAllUserProfiles(String sortBy, String limit) {
        int resultLimit = StringUtils.isNullOrEmpty(limit) ? 100 : Integer.valueOf(limit);
        String sortParam = StringUtils.isNullOrEmpty(sortBy) ? DEFAULT_SORT_ORDER : sortBy;
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, sortParam));
        Iterable<UserProfile> users = userProfileRepo.findAll(sortCriteria);
        ArrayList<UserProfile> userList = new ArrayList<>();
        if (!users.iterator().hasNext()) {
            return createUserProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        users.forEach(each -> {
            if (userList.size() < resultLimit) {
                userList.add(each);
            }
        });
        return createUserProfileResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse getUserProfilesByZip(String zipCode) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER));
        Page<UserProfile> results = userProfileRepo.findByZip(zipCode, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, sort));
        long numResults = results.getTotalElements();
        LOGGER.info("Number of results with zipcode {}: {}", zipCode,  numResults);
        if (numResults > 0) {
            return createUserProfileResponse(true, results.getContent(), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return createUserProfileResponse(true, Collections.emptyList(),
                HttpStatus.NO_CONTENT, String.format("No users found with zipcode %s", zipCode));

    }

    public UserProfileResponse findUserProfileById(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(user.get())), HttpStatus.OK,
                    DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User profile with id={} not found", id);

        return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND, id));
    }

    public UserProfileResponse findUserProfileByUserAccountEmail(String email) {
        UserAccount userAccount = userAccountRepo.findByUsername(email);
        Optional<UserProfile> userProfile = userProfileRepo.findByUserAccount(userAccount);
        if (userProfile.isPresent()) {
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

        return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfile(Long userId, final UserProfile userProfile) {
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (!result.isPresent()) {
            LOGGER.error("Error updating user profile for id={}: invalid request", userId);

            return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format("UpdateUserProfile error: %s", NOT_FOUND));
        }
        else if (!userId.equals(userProfile.getId())) {
            return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        userProfileRepo.save(userProfile);

        LOGGER.info("User profile with id={} was found, updating existing profile", userId);
        return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(userProfile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateLastLoginForUserProfile(Long userId, String lastLogin) {
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (result.isPresent() && lastLogin.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            userProfileRepo.updateLastLogin(userId, Date.valueOf(lastLogin));

            return createUserProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return createUserProfileResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }


        public UserProfileResponse deleteUserProfileById(Long id) {
        Optional<UserProfile> result = userProfileRepo.findById(id);
        if (!result.isPresent()) {
            LOGGER.error("Error: user profile with id={} not found", id);
            return createUserProfileResponse( false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(USER_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id={} was deleted successfully", id);

        return createUserProfileResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    private boolean userProfileFailsNullPointerCheck(UserProfile userProfile) {
        return userProfile == null ||
                userProfile.getId() == null ||
                userProfile.getUserAccount() == null;
    }

}
