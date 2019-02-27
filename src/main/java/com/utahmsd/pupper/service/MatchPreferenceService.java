package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.MatchPreferenceRepo;
import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.MatchPreferenceResponse;
import com.utahmsd.pupper.dto.pupper.PreferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                return MatchPreferenceResponse.createMatchPreferenceResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
            }
            MatchPreference preference = matchPreferenceRepo.save(matchPreference);
            results.add(preference);
        }
        return MatchPreferenceResponse.createMatchPreferenceResponse(true, results, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchPreferenceResponse getMatchPreferencesByMatchProfileId(Long matchProfileId) {
       List<MatchPreference> matchPreferences = matchPreferenceRepo.findAllByMatchProfile_Id(matchProfileId);
       if (matchPreferences.isEmpty()) {
           return MatchPreferenceResponse.createMatchPreferenceResponse(false, null, HttpStatus.NOT_FOUND, NO_QUERY_RESULTS);
       }
       return MatchPreferenceResponse.createMatchPreferenceResponse(true, matchPreferences, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public MatchPreferenceResponse getMatchPreferencesByMatchProfileIdFilterByType(Long matchProfileId, String preferenceType) {
        List<MatchPreference> matchPreferences = matchPreferenceRepo.findAllByMatchProfile_IdAndPreferenceType(matchProfileId,
                                                                                    PreferenceType.fromValue(preferenceType));
        if (matchPreferences.isEmpty()) {
            return MatchPreferenceResponse.createMatchPreferenceResponse(false, null, HttpStatus.NOT_FOUND, NO_QUERY_RESULTS);
        }
        return MatchPreferenceResponse.createMatchPreferenceResponse(true, matchPreferences, HttpStatus.OK, DEFAULT_DESCRIPTION);
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

    public MatchPreferenceResponse updateMatchPreferenceById(Long matchProfileId, Long matchPreferenceId,
                                                             MatchPreference preference) {
        MatchPreference matchPreference = matchPreferenceRepo.findByMatchProfile_IdAndId(matchProfileId, matchPreferenceId);
        if (matchPreference == null) {
            return MatchPreferenceResponse.createMatchPreferenceResponse(false, null, HttpStatus.NOT_FOUND,
                    INVALID_PATH_VARIABLE);
        }
        matchPreferenceRepo.updateMatchPreference(preference.getPreferenceType(), preference.getMatchingPreference(), matchPreferenceId);
        Optional<MatchPreference> result = matchPreferenceRepo.findById(matchPreferenceId);
        if (result.isPresent()) {
            return MatchPreferenceResponse.createMatchPreferenceResponse(true, Arrays.asList(result.get()), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }
        return MatchPreferenceResponse.createMatchPreferenceResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_INPUT);
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
        return MatchPreferenceResponse.createMatchPreferenceResponse(true, savedRecords, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }
}
