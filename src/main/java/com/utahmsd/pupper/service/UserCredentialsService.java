package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserCredentials;
import com.utahmsd.pupper.dto.User;
import com.utahmsd.pupper.dto.UserCredentialsRequest;
import com.utahmsd.pupper.dto.UserCredentialsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserCredentialsResponse.createUserCredentialsResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Utils.generateHash;
import static com.utahmsd.pupper.util.Utils.generateSaltString;

@Named
@Singleton
public class UserCredentialsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredentialsService.class);
    private static final String INVALID_CREDENTIALS_ID = "User credentials with id %d not found";
    private static final String INVALID_EMAIL = "User credentials with email %s not found";
    private static final String INVALID_LOGIN = "Invalid login credentials were entered.";

    private static final int NUM_SALT_BYTES = 32;

    @Inject
    private UserCredentialsRepo userCredentialsRepo;

    @Inject
    private UserProfileRepo userProfileRepo;

    public UserCredentialsResponse getAllUserCredentials() {
        Iterable<UserCredentials> resultList = userCredentialsRepo.findAll();
        List<User> userList = new ArrayList<>();
        if (resultList.iterator().hasNext()) {
            resultList.forEach(each -> userList.add(
                    new User(each.getUser().getEmail(), null))); //Hide sensitive data field

            return createUserCredentialsResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        return createUserCredentialsResponse(
                false, userList, HttpStatus.NO_CONTENT, "No user credentials were found.");
    }

    public UserCredentialsResponse findUserCredentials(Long userId) {
        Optional<UserCredentials> result = userCredentialsRepo.findById(userId);
        List<User> userList = new ArrayList<>();
        if (result.isPresent()) {
            userList.add(new User(result.get().getUser().getEmail(), null)); //Hide sensitive data field
            return createUserCredentialsResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User credentials with id {} not found", userId);

        return createUserCredentialsResponse(
                false, userList, HttpStatus.NOT_FOUND, String.format(INVALID_CREDENTIALS_ID, userId));
    }

    public UserCredentialsResponse createUserCredentials(UserCredentialsRequest request) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser(request.getUser());
        String salt = generateSaltString(NUM_SALT_BYTES);
        userCredentials.setSalt(salt);
        userCredentials.setDateJoined(Date.valueOf(LocalDate.now()));
        userCredentials.setComputedHash(generateHash(request.getUser().getPassword(), salt.getBytes()));
        UserCredentials userCredentialsResult = userCredentialsRepo.save(userCredentials);
        ArrayList<User> userList = new ArrayList<>();
        userList.add(
                new User(userCredentialsResult.getUser().getEmail(), null)); //Hide sensitive data field
        return createUserCredentialsResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserCredentialsResponse authenticateUser(UserCredentialsRequest request) {
        String email = request.getUser().getEmail();
        String password = request.getUser().getPassword();
        Optional<UserCredentials> result = userCredentialsRepo.findByEmail(email);
        if (!result.isPresent()) {
            LOGGER.error(String.format(INVALID_EMAIL, email));

            return createUserCredentialsResponse(false, Collections.emptyList(), HttpStatus.NOT_FOUND, String.format(INVALID_EMAIL, email));
        }
        ArrayList<User> user = new ArrayList<>();
        user.add(new User(email, null));

        UserCredentials userCredentials = result.get();
        String computedHashFromSubmittedPassword = generateHash(password, userCredentials.getSalt().getBytes());
        if (userCredentials.getComputedHash().equals(computedHashFromSubmittedPassword)) { //Login success

            return createUserCredentialsResponse(true, user, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        //Otherwise, invalid login credentials
        return createUserCredentialsResponse(false, user, HttpStatus.UNAUTHORIZED, INVALID_LOGIN);

    }

//    public UserCredentialsResponse findUserCredentialsByUserProfileId(Long userProfileId) {
//
//     }


}
