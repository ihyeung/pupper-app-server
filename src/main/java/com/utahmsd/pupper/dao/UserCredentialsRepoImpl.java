package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.List;

public class UserCredentialsRepoImpl implements UserCredentialsRepo {
    @Override
    public Option<UserAuthenticationRequest> find(Long aLong) {
        return null;
    }

    @Override
    public Option<List<UserAuthenticationRequest>> findAll() {
        return null;
    }

    @Override
    public Option<UserAuthenticationRequest> save(UserAuthenticationRequest userAuthenticationRequest) {
        return null;
    }

    @Override
    public Option<UserAuthenticationRequest> delete(UserAuthenticationRequest userAuthenticationRequest) {
        return null;
    }
}
