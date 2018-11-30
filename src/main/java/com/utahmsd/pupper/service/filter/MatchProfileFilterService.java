package com.utahmsd.pupper.service.filter;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.utahmsd.pupper.dto.MatchProfileResponse.createMatchProfileResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.IDS_MISMATCH;
import static com.utahmsd.pupper.util.Constants.INVALID_PATH_VARIABLE;

@Named
@Singleton
public class MatchProfileFilterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchProfileFilterService.class);

    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    public MatchProfileFilterService(MatchProfileRepo matchProfileRepo) {
        this.matchProfileRepo = matchProfileRepo;
    }

    public MatchProfileResponse getAllMatchProfilesByEmail(String email) {
        return null;
    }

}
