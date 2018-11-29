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

    public UserProfileResponse getAllUserProfiles(String sortBy, String limit) {
        int resultLimit = StringUtils.isNullOrEmpty(limit) ? DEFAULT_QUERY_LIMIT : Integer.valueOf(limit);
        String sortParam = StringUtils.isNullOrEmpty(sortBy) ? DEFAULT_SORT_ORDER : sortBy;
        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, sortParam));
        Iterable<UserProfile> users = userProfileRepo.findAll(sortCriteria);
        ArrayList<UserProfile> userList = new ArrayList<>();
        if (!users.iterator().hasNext()) {
            return createUserProfileResponse(true, null, HttpStatus.OK, NO_QUERY_RESULTS);
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
        LOGGER.info("No user profiles found with zipcode '%d'", zipCode);
        return createUserProfileResponse(true, null, HttpStatus.OK, NO_QUERY_RESULTS);

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

    public UserProfileResponse findUserProfileByUserAccountEmail(String email) {
        UserAccount userAccount = userAccountRepo.findByUsername(email);
        Optional<UserProfile> userProfile = userProfileRepo.findByUserAccount(userAccount);
        if (userProfile.isPresent()) {
            return createUserProfileResponse(true, new ArrayList<>(Arrays.asList(userProfile.get())),
                    HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error(String.format(EMAIL_NOT_FOUND, "User profile", email));

        return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND,
                String.format(EMAIL_NOT_FOUND, "User profile", email));
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

    public UserProfileResponse updateUserProfileByUserAccountEmail(String email, UserProfile userProfile) {
        UserProfile result = userProfileRepo.findByUserAccount_Username(email);
        if (result == null) {
            LOGGER.error(EMAIL_NOT_FOUND, "User profile", email);

            return createUserProfileResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMAIL_NOT_FOUND, "User profile", email));
        }
        userProfile.setUserAccount(result.getUserAccount()); //Reuse userAccount reference to avoid TransientObjectException caused by unintentionally trying to modify the userAccount too
        userProfile.setId(result.getId());
        userProfileRepo.save(userProfile);

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
