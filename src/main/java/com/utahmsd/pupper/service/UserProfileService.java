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
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static com.utahmsd.pupper.util.ValidationUtils.isValidDate;


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

    public UserProfileResponse findUserProfileById(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(user.get())), HttpStatus.OK,
                    DEFAULT_DESCRIPTION);
        }
        LOGGER.error(ID_NOT_FOUND, "User profile", id);

        return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND,
                String.format(USER_PROFILE_NOT_FOUND, id));
    }

    public UserProfileResponse createOrUpdateUserProfile(UserProfile userProfile) {
        UserProfile result = userProfileRepo.findByUserAccount_Username(userProfile.getUserAccount().getUsername());
        if (result != null) {
            LOGGER.info("User profile already exists. Updating existing user profile with id={}", result.getId());

            userProfile.setUserAccount(result.getUserAccount());
            userProfile.setId(result.getId());
            userProfileRepo.save(userProfile);

            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(userProfile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.info("Creating a new user profile.");
        UserProfile profile = userProfileRepo.save(userProfile);

        return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(profile)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfileByUserProfileId(Long userId, UserProfile userProfile) {
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (!result.isPresent()) {
            LOGGER.error(ID_NOT_FOUND, "User profile", userId);

            return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        else if (!userId.equals(userProfile.getId())) {
            LOGGER.error(IDS_MISMATCH);
            return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        userProfile.setUserAccount(result.get().getUserAccount());
        userProfile.setId(result.get().getId());
        userProfileRepo.save(userProfile);

        LOGGER.info("User profile with id={} was found, updating existing profile", userId);
        return createUserProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateLastLoginForUserProfile(Long userProfileId, String lastLogin) {
        if (!isValidDate(lastLogin)) {
            return createUserProfileResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY, INVALID_INPUT);
        }
        Optional<UserProfile> result = userProfileRepo.findById(userProfileId);
        if (result.isPresent()) {
            userProfileRepo.updateLastLogin(userProfileId, Date.valueOf(lastLogin));
            return createUserProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }

    public UserProfileResponse deleteUserProfileById(Long id) {
        Optional<UserProfile> result = userProfileRepo.findById(id);
        if (!result.isPresent()) {
            LOGGER.error(ID_NOT_FOUND, "User profile", id);
            return createUserProfileResponse( false, null, HttpStatus.NOT_FOUND,
                    String.format(USER_PROFILE_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id={} was deleted successfully", id);
        return createUserProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
