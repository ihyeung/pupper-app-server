package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.List;

public class PupperProfileRepoImpl implements PupperProfileRepo {
    @Override
    public List<PupperProfileResponse> findAllByUserId(int userId) {
        return null;
    }

    @Override
    public PupperProfileResponse findByPupId(int pupId) {
        return null;
    }

    @Override
    public PupperProfileResponse insertOrUpdateProfile(PupperProfileRequest request) {
        return null;
    }

    @Override
    public Option<PupperProfileRequest> find(Long aLong) {
        return null;
    }

    @Override
    public Option<List<PupperProfileRequest>> findAll() {
        return null;
    }

    @Override
    public Option<PupperProfileRequest> save(PupperProfileRequest pupperProfileRequest) {
        return null;
    }

    @Override
    public Option<PupperProfileRequest> delete(PupperProfileRequest pupperProfileRequest) {
        return null;
    }
}
