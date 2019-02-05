package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchPreferenceRepo;
import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.MatchPreferenceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.utahmsd.pupper.util.Constants.*;

@Service
public class MatchPreferenceService {

    private  final MatchPreferenceRepo matchPreferenceRepo;

    @Autowired
    public MatchPreferenceService(MatchPreferenceRepo matchPreferenceRepo) {
        this.matchPreferenceRepo = matchPreferenceRepo;
    }

    public MatchPreferenceResponse insertMatchPreferences(Long matchProfileId, List<MatchPreference> matchPreferences) {
        List<MatchPreference> results = new ArrayList<>();
        for (MatchPreference matchPreference: matchPreferences) {
            if (!matchProfileId.equals(matchPreference.getMatchProfile().getId())) {
                return MatchPreferenceResponse.createResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
            }
            MatchPreference preference = matchPreferenceRepo.save(matchPreference);
            results.add(preference);
        }
        return MatchPreferenceResponse.createResponse(true, results, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchPreferenceResponse getMatchPreferences(Long matchProfileId) {
       List<MatchPreference> matchPreferences = matchPreferenceRepo.findAllByMatchProfile_Id(matchProfileId);
       if (matchPreferences.isEmpty()) {
           return MatchPreferenceResponse.createResponse(false, null, HttpStatus.NOT_FOUND, NO_QUERY_RESULTS);
       }
       return MatchPreferenceResponse.createResponse(true, matchPreferences, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchPreferenceResponse updateMatchPreferences(Long matchProfileId, List<MatchPreference> matchPreferences) {
        matchPreferences.forEach(each -> {

        });
        return MatchPreferenceResponse.createResponse(true, matchPreferences, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public void deleteMatchPreferences(Long matchProfileId) {
        matchPreferenceRepo.deleteAllByMatchProfile_Id(matchProfileId);
    }
}
