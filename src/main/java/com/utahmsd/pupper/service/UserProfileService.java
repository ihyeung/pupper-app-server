package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserProfileService implements ProfileService {

//    @Inject
//    UserProfileRepo userProfileRepo;

//    @Inject
    UserCredentialsRepo userCredentialsRepo;

    public UserAuthenticationResponse createUser (UserAuthenticationRequest request) {
        UserAccount userAccount = userCredentialsRepo.createUserAccount(request);
        return new UserAuthenticationResponse().fromUserAccount(userAccount);
    }

    public UserAuthenticationResponse authenticateUser (UserAuthenticationRequest request) {
        return null;
    }

    public UserProfileResponse findUserProfile (int userId) {
        return null;
    }

}
