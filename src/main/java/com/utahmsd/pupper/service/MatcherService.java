package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;

import javax.inject.Inject;

public class MatcherService {

    @Inject
    MatchProfileRepo matchProfileRepo;

    @Inject
    PupperProfileRepo pupperProfileRepo;

    @Inject
    UserProfileRepo userProfileRepo;


}
