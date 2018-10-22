package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepo;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.Valid;

@Named
@Singleton
public class UserAuthenticationService {

    public UserAuthenticationResponse getAuthToken() {
        return null;
    }

//    @Inject
//    UserCredentialsRepo userCredentialsRepo;
//
//
//    public UserAuthenticationResponse authenticateUserCredentials (@Valid UserAuthenticationRequest request) {
//        return null;
//    }
//
//    public UserAuthenticationResponse createUserCredentials (UserAuthenticationRequest request) {
//        return null;
//    }
//
//    public UserAuthenticationResponse updateUserCredentials (Long credentialsId, UserAuthenticationRequest request) {
//        return null;
//    }
//
//    public UserAuthenticationResponse deleteUserCredentials (Long credentialsId) {
//        return null;
//    }

}
