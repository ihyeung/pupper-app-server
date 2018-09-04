package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.PupperProfile;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Named
@Singleton
public class PupperProfileService {

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;

    //Basic methods

    public PupperProfileResponse findPupperProfile(Long id) {
        Optional<PupperProfile> pupperProfile = pupperProfileRepo.findById(id);
        return null;
    }

    public PupperProfileResponse createOrUpdatePupperProfile(PupperProfileRequest request) {
        return null;
    }

    //Methods for filtering by criteria

    public PupperProfileResponse filterPupperProfileById (Long matchProfileId, Long userId) {
        if (matchProfileId == null && userId == null) {
            Iterable<PupperProfile> pupperProfileList = pupperProfileRepo.findAll();
//            pupperProfileList.forEach();
        }
        return null;
    }

    public List<PupperProfileResponse> findAllByUserId(Long id) {
        Optional<List<PupperProfile>> pupperProfileList = pupperProfileRepo.findAllByUserId(id);
        return null;
    }

}
