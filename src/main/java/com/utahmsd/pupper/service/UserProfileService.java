package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserProfileResponse.createUserProfileResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static com.utahmsd.pupper.util.ValidationUtils.isValidDate;


@Service
public class UserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepo userProfileRepo;

    @Autowired
    UserProfileService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }

    public UserProfileResponse findUserProfileById(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            return createUserProfileResponse(true, Arrays.asList(user.get()), HttpStatus.OK,
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
            UserProfile savedResult = userProfileRepo.save(userProfile);

            return createUserProfileResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.info("Creating a new user profile.");
        UserProfile profile = userProfileRepo.save(userProfile);

        return createUserProfileResponse(true, Arrays.asList(profile), HttpStatus.OK, DEFAULT_DESCRIPTION);
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
        UserProfile savedResult = userProfileRepo.save(userProfile);

        LOGGER.info("User profile with id={} was found, updating existing profile", userId);
        return createUserProfileResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
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

    public UserProfileResponse updateProfileImageById(Long userId, String imageUrl) {
        userProfileRepo.updateProfileImage(userId, imageUrl);
        return createUserProfileResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

//    @Transactional
    public void deleteUserProfileByEmail(String email) {
        userProfileRepo.deleteByUserAccount_Username(email);
    }
}
