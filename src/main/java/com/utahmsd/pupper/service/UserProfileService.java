package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.UserProfileRequest;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named
@Singleton
public class UserProfileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);


    @Inject
    UserProfileRepo userProfileRepo;

    public UserProfileResponse getAllUserProfiles() {
        Iterable<UserProfile> users = userProfileRepo.findAll();
        UserProfileResponse response = new UserProfileResponse();
        if (users.iterator().hasNext()) {
            response.setSuccess(true);
            response.setStatusCode(HttpStatus.OK);
            users.forEach(u->response.getUserProfile().add(u));
            response.setId(Long.parseLong("1000"));
            return response;
        }
        response.setStatusCode(HttpStatus.NO_CONTENT);
        response.setSuccess(false);
        response.setId(Long.parseLong("-1"));
        return response;
    }

//    public UserProfileResponse findUserProfile(Long id) {
//        Optional<UserProfile> user = userProfileRepo.findById(id);
//        UserProfileResponse response = new UserProfileResponse();
//        if (null != user.get()) {
//            response.setId(id);
//            response.getUserProfile().add(user.get());
//            response.setStatusCode(HttpStatus.FOUND);
//            response.setSuccess(true);
//            return response;
//        }
//        LOGGER.info("User profile with id {} not found", id);
//        response.setStatusCode(HttpStatus.NOT_FOUND);
//        response.setStatus(String.format("User profile with id {} not found", id));
//        response.setSuccess(false);
//        return response;
////        return response.responseErrorHandler(request.getId(), new Exception(), request);
//    }
//
//    public UserProfileResponse createOrUpdateProfile(UserProfileRequest request) {
//        UserProfileResponse userProfileResponse = findUserProfile(request.getId());
//        if (userProfileResponse.getUserProfile() != null) { //Profile already exists, just update
//            LOGGER.info("User profile with id {} was found, updating existing profile", request.getId());
//            userProfileResponse.setStatusCode(HttpStatus.OK);
//
//        } else {
//            LOGGER.info("User profile with id {} not found, new profile created", request.getId());
//            userProfileResponse.setStatusCode(HttpStatus.CREATED);
//        }
//        userProfileRepo.save(request.getUserProfile());
//        return userProfileResponse;
//    }
//
//    public UserProfileResponse deleteProfile(UserProfileRequest request) {
//        UserProfileResponse userProfileResponse = findUserProfile(request.getId());
//        if (userProfileResponse.getUserProfile() != null) { //Profile exists for deletion
//            LOGGER.info("User profile with id {} was found, deleting profile", request.getId());
//
//            userProfileRepo.deleteById(request.getId());
//            userProfileResponse.setUserProfile(null);
//            userProfileResponse.setSuccess(true);
//            userProfileResponse.setStatusCode(HttpStatus.OK);
//        } else {
//            LOGGER.info("User profile with id {} not found", request.getId());
//
//            userProfileResponse.setSuccess(false);
//            userProfileResponse.setStatusCode(HttpStatus.NO_CONTENT);
//        }
//        return userProfileResponse;
//
//    }
}
