package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepoImpl;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserAuthenticationService {
    @Inject
    UserCredentialsRepoImpl userCredentialsRepo;

    public UserAuthenticationResponse authenticateUser (UserAuthenticationRequest request) {
        return null;
    }
}
