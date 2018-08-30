package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.ProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;

import java.util.List;

public interface PupperProfileRepo extends Repository<PupperProfileRequest, Long> {
    List<PupperProfileResponse> findAllByUserId(int userId);
    PupperProfileResponse findByPupId(int pupId);
    PupperProfileResponse insertOrUpdateProfile(PupperProfileRequest request);

}
