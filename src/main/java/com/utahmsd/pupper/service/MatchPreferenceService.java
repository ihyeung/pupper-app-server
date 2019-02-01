package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchPreferenceRepo;
import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.MatchPreferenceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_PATH_VARIABLE;

@Service
public class MatchPreferenceService {

    private  final MatchPreferenceRepo matchPreferenceRepo;

    @Autowired
    public MatchPreferenceService(MatchPreferenceRepo matchPreferenceRepo) {
        this.matchPreferenceRepo = matchPreferenceRepo;
    }

    public MatchPreferenceResponse insertMatchPreference(Long matchProfileId, MatchPreference matchPreference) {
        if (!matchProfileId.equals(matchPreference.getMatchProfile().getId())) {
            return MatchPreferenceResponse.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        MatchPreference preference = matchPreferenceRepo.save(matchPreference);
        return MatchPreferenceResponse.createResponse(true, Arrays.asList(preference), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
