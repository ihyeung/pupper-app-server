package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.PupperProfileRequest;
import com.utahmsd.pupper.dto.PupperProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Named
@Singleton
public class PupperProfileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;

    public PupperProfileResponse getAllPupperProfiles() {
        return null;
    }

    public PupperProfileResponse findAllPupperProfilesByUserId(Long userId) {
        return null;
    }

    public PupperProfileResponse findPupperProfile(Long pupperId, Long userId) {
        return null;
    }

    public PupperProfileResponse createNewPupperProfile(PupperProfileRequest request) {
        return null;
    }

    public PupperProfileResponse updatePupperProfile(PupperProfileRequest request) {
        return null;
    }

    public PupperProfileResponse deletePupperProfile(Long userId, Long pupperId) {
        return null;
    }

}