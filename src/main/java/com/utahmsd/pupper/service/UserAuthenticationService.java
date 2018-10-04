package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserCredentialsRepo;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserAuthenticationService {

    @Inject
    UserCredentialsRepo userCredentialsRepo;

    @Inject
    BCryptPasswordEncoder encoder;

    public UserAuthenticationResponse authenticateUser (UserAuthenticationRequest request) {
        return null;
    }
}
