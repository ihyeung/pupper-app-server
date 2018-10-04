package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserCredentials;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Named
@Singleton
public class UserCredentialsRepoImpl implements UserCredentialsRepo {

    @Override
    public UserCredentials save(UserCredentials userCredentials) {
        return null;
    }

    @Override
    public Optional<UserCredentials> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserCredentials> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByEmail(String email) {

    }
}