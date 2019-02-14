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

    /**
     * Service method that permanently replaces all match_preference data for a given matchProfileId with a
     * provided list of matchPreferences.
     *
     * Any matchPreference records initially contained in match_preference that are not included in the provided list
     * will be permanently deleted.
     * @param matchProfileId
     * @param matchPreferences
     * @return
     */
    public MatchPreferenceResponse updateAllMatchPreferences(Long matchProfileId, List<MatchPreference> matchPreferences) {
        List<MatchPreference> queryResult = matchPreferenceRepo.findAllByMatchProfile_Id(matchProfileId);
        if (queryResult.isEmpty()) {
            return insertMatchPreferenceData(matchPreferences);
        }
        deleteMatchPreferences(matchProfileId); //Permanently reset match preference data for the user
        return insertMatchPreferenceData(matchPreferences);
    }

    public void deleteMatchPreferences(Long matchProfileId) {
        matchPreferenceRepo.deleteAllByMatchProfile_Id(matchProfileId);
    }

    private MatchPreferenceResponse insertMatchPreferenceData(List<MatchPreference>  matchPreferences) {
        List<MatchPreference> savedRecords = new ArrayList<>();
        matchPreferences.forEach(each -> {
            MatchPreference saved = matchPreferenceRepo.save(each);
            savedRecords.add(saved);
        });
        return MatchPreferenceResponse.createResponse(true, savedRecords, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
