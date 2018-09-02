package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfile;
import com.utahmsd.pupper.dao.UserProfileRepoImpl;
import com.utahmsd.pupper.dto.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named
@Singleton
public class UserProfileService {

    @Inject
    UserProfileRepoImpl userProfileRepo;

//    public UserProfileResponse createUserProfile (UserProfileRequest request) {
//        UserProfile userProfile = new UserProfile(request);
//        userProfileRepo.save(userProfile);
//        return new UserProfileResponse(userProfile);
//    }
//
//    public UserProfileResponse findUserProfile (UserProfileRequest request) {
//        Optional<UserProfile> user = userProfileRepo.find(request.getId());
//        return user.isPresent() ? new UserProfileResponse(user.get()) : null;
//    }
//
//    public UserProfileResponse updateUserProfile (UserProfileRequest request) {
//        Optional<UserProfile> userProfile = userProfileRepo.find(request.getId());
//        //TODO
//        return null;
//    }
//
//    public UserProfileResponse deleteUserProfile (UserProfileRequest request) {
//        Optional<UserProfile> userProfile = userProfileRepo.find(request.getId());
//        //TODO
//        return null;
//    }

    public UserProfileResponse createProfile(UserProfileRequest request) {
        userProfileRepo.save(request.getUserProfile());
        return UserProfileResponse.fromUserProfileRequest(request);
    }

    public UserProfileResponse findUserProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.find(id);
        if (user.isPresent()) {
            UserProfileResponse response = new UserProfileResponse();
            response.setId(id);
            response.setUserProfile(user.get());
            response.setSuccess(true);
            response.setStatus("hello found");
            return response;
        }
        return new UserProfileResponse(id, "error finding profile");
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        Optional<UserProfile> user = userProfileRepo.find(request.getId());
        if (user.isPresent()) {
            userProfileRepo.update(request.getUserProfile());
            return UserProfileResponse.fromUserProfileRequest(request);
        }
        return createProfile(request);
    }

    public UserProfileResponse deleteProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.find(id);
        if (user.isPresent()) {
            userProfileRepo.delete(user.get());
            UserProfileResponse response = new UserProfileResponse();
            response.setId(id);
            response.setUserProfile(user.get());
            response.setSuccess(true);
            response.setStatus("hello deleted");
            return response;
        }
        return new UserProfileResponse(id, "profile does not exist and cannot be deleted");
    }
}
