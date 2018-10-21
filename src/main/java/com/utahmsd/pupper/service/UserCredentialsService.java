package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserCredentials;
import com.utahmsd.pupper.dto.UserCredentialsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserCredentialsResponse.createUserCredentialsResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;

@Named
@Singleton
public class UserCredentialsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredentialsService.class);
    private static final String INVALID_CREDENTIALS = "User credentials with %d not found";

    @Inject
    private UserCredentialsRepo userCredentialsRepo;

    @Inject
    private UserProfileRepo userProfileRepo;

    public UserCredentialsResponse getAllUserCredentials() {
//        Sort sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "dateJoined"));
//        Iterable<UserCredentials> resultList = userCredentialsRepo.findAll(sortCriteria);
        Iterable<UserCredentials> resultList = userCredentialsRepo.findAll();

        List<UserCredentials> userCredentialsList = new ArrayList<>();
        if (resultList.iterator().hasNext()) {
            resultList.forEach(userCredentialsList::add);
            return createUserCredentialsResponse(true, userCredentialsList, HttpStatus.OK, DEFAULT_DESCRIPTION);

        }

        return createUserCredentialsResponse(
                false, userCredentialsList, HttpStatus.NO_CONTENT, "No user credentials were found.");
    }

    public UserCredentialsResponse findUserCredentials(Long userId) {
        Optional<UserCredentials> result = userCredentialsRepo.findById(userId);
        List<UserCredentials> userCredentials = new ArrayList<>();
        if (result.isPresent()) {
            userCredentials.add(result.get());
            return createUserCredentialsResponse(true, userCredentials, HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        LOGGER.error("User credentials with id {} not found", userId);

        return createUserCredentialsResponse(
                false, userCredentials, HttpStatus.NOT_FOUND, String.format(INVALID_CREDENTIALS, userId));
    }

//    public UserCredentialsResponse findUserCredentialsByUserProfileId(Long userProfileId) {
//
//     }
}
