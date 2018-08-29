package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.UserProfileRequest;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.List;

public class UserProfileRepoImpl implements UserProfileRepo {
    @Override
    public Option<UserProfileRequest> find(Long aLong) {
        return null;
    }

    @Override
    public Option<List<UserProfileRequest>> findAll() {
        return null;
    }

    @Override
    public Option<UserProfileRequest> save(UserProfileRequest userProfileRequest) {
        return null;
    }

    @Override
    public Option<UserProfileRequest> delete(UserProfileRequest userProfileRequest) {
        return null;
    }
}
