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
        UserProfileResponse response = new UserProfileResponse();
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            return response.createResponse(true, Arrays.asList(user.get()), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error(ID_NOT_FOUND, "User profile", id);

        return response.createResponse(false, null, HttpStatus.NOT_FOUND,
                String.format(USER_PROFILE_NOT_FOUND, id));
    }

    public UserProfileResponse createUserProfile(UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse();
        UserProfile result = userProfileRepo.findByUserAccount_Username(userProfile.getUserAccount().getUsername());
        if (result != null) {
            LOGGER.error("A user profile for that username already exists, userProfileId={}", result.getId());
            return response.createResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_INPUT);
        }
        UserProfile profile = userProfileRepo.save(userProfile);
        return response.createResponse(true, Arrays.asList(profile), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateUserProfileByUserProfileId(Long userId, UserProfile userProfile) {
        UserProfileResponse response = new UserProfileResponse();
        if (!userId.equals(userProfile.getId())) {
            LOGGER.error(IDS_MISMATCH);
            return response.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (!result.isPresent()) {
            LOGGER.error(USER_PROFILE_NOT_FOUND, userId);
            return response.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        userProfile.setUserAccount(result.get().getUserAccount());
        userProfile.setId(result.get().getId());
        UserProfile savedResult = userProfileRepo.save(userProfile);

        return response.createResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateLastLoginForUserProfile(Long userProfileId, String lastLogin) {
        UserProfileResponse response = new UserProfileResponse();
        if (!isValidDate(lastLogin)) {
            return response.createResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY, INVALID_INPUT);
        }
        Optional<UserProfile> result = userProfileRepo.findById(userProfileId);
        if (result.isPresent()) {
            Date lastLoginUpdate = Date.valueOf(lastLogin);
            userProfileRepo.updateLastLogin(userProfileId, lastLoginUpdate);
            UserProfile updatedUser = result.get();
            updatedUser.setLastLogin(lastLoginUpdate);
            return response.createResponse(true, Arrays.asList(updatedUser), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return response.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }

    public UserProfileResponse deleteUserProfileById(Long id) {
        UserProfileResponse response = new UserProfileResponse();

        Optional<UserProfile> result = userProfileRepo.findById(id);
        if (!result.isPresent()) {
            LOGGER.error(ID_NOT_FOUND, "User profile", id);
            return response.createResponse( false, null, HttpStatus.NOT_FOUND,
                    String.format(USER_PROFILE_NOT_FOUND, id));
        }
        userProfileRepo.deleteById(id);
        LOGGER.info("User profile with id={} was deleted successfully", id);
        return response.createResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserProfileResponse updateProfileImageById(Long userId, String imageUrl) {
        UserProfileResponse response = new UserProfileResponse();
        Optional<UserProfile> result = userProfileRepo.findById(userId);
        if (result.isPresent()) {
            userProfileRepo.updateProfileImage(userId, imageUrl);
            result.get().setProfileImage(imageUrl);
            return response.createResponse(true, Arrays.asList(result.get()), HttpStatus.OK, DEFAULT_DESCRIPTION);

        }
        return response.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
    }

    public void deleteUserProfileByEmail(String email) {
        userProfileRepo.deleteByUserAccount_Username(email);
    }
}
