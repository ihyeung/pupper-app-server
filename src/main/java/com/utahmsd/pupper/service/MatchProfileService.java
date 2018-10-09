package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dto.MatchProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class MatchProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileService.class);
    private static final String DEFAULT_SORT_BY_CRITERIA = "breed_id";

    public MatchProfileResponse getAllMatchProfiles() {

        return null;
    }
}
