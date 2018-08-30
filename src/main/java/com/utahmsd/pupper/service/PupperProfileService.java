package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Named
@Singleton
public class PupperProfileService implements ProfileService {

//    @Inject
    PupperProfileRepo pupperProfileRepo;

    public PupperProfileResponse findPupperProfile(int id) {
        return pupperProfileRepo.findByPupId(id);
    }

    public List<PupperProfileResponse> findAllByUserId(int id) {
        return pupperProfileRepo.findAllByUserId(id);
    }

    public PupperProfileResponse createOrUpdatePupperProfile(PupperProfileRequest request) {
        return pupperProfileRepo.insertOrUpdateProfile(request);
    }

}
