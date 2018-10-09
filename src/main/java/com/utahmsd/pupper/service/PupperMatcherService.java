package com.utahmsd.pupper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dto.PupperMatcherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class PupperMatcherService {

    private static final int DEFAULT_BATCH_SIZE = 50; //Number of match profiles to retrieve for a user per batch

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);


    @Inject
    MatchProfileRepo matchProfileRepo;

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;

    public PupperMatcherResponse getMatches(Long matchProfileId) {
        return null;
    }
}
