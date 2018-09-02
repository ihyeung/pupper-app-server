package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserProfile;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named
@Singleton
public class UserProfileService {

    @Inject
    UserProfileRepo userProfileRepo;

    public UserProfileResponse createProfile(UserProfileRequest request) {
        userProfileRepo.save(request.getUserProfile());
        UserProfileResponse response = UserProfileResponse.fromUserProfileRequest(request);
        response.setStatus("created");
        return response;
    }

    public UserProfileResponse findUserProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            return new UserProfileResponse(id);
        }
        return new UserProfileResponse(id, "error finding profile");
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        Optional<UserProfile> user = userProfileRepo.findById(request.getId());
        if (user.isPresent()) {
            userProfileRepo.save(request.getUserProfile());
            UserProfileResponse response = UserProfileResponse.fromUserProfileRequest(request);
            response.setStatus("updated");
        }
        return createProfile(request);
    }

    public UserProfileResponse deleteProfile(Long id) {
        Optional<UserProfile> user = userProfileRepo.findById(id);
        if (user.isPresent()) {
            userProfileRepo.delete(user.get());
            UserProfileResponse response = new UserProfileResponse(id);
            response.setStatus("deleted");
            return response;
        }
        return new UserProfileResponse(id, "profile does not exist and cannot be deleted");
    }
}
