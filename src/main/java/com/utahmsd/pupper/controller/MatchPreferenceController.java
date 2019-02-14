package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.MatchPreferenceResponse;
import com.utahmsd.pupper.service.MatchPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MatchPreferenceController {

    private final MatchPreferenceService matchPreferenceService;

    @Autowired
    MatchPreferenceController(MatchPreferenceService matchPreferenceService) {
        this.matchPreferenceService = matchPreferenceService;
    }

    @PostMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse insertMatchPreferences(@PathVariable("matchId") Long matchProfileId,
                                                         @RequestBody @Valid List<MatchPreference> matchPreferences) {
        return matchPreferenceService.insertMatchPreferences(matchProfileId, matchPreferences);
    }

    @GetMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse getMatchPreferences(@PathVariable("matchId") Long matchProfileId) {
        return matchPreferenceService.getMatchPreferences(matchProfileId);
    }

    @PutMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse updateAllMatchPreferences(@PathVariable("matchId") Long matchProfileId,
                                                       @RequestBody @Valid List<MatchPreference> matchPreferences) {
        return matchPreferenceService.updateAllMatchPreferences(matchProfileId, matchPreferences);
    }

    @PutMapping(path = "/matchProfile/{matchId}/matchPreference/{matchPreferenceId}")
    public MatchPreferenceResponse updateMatchPreference(@PathVariable("matchId") Long matchProfileId,
                                                          @PathVariable("matchPreferenceId") Long preferenceId,
                                                          @RequestBody @Valid MatchPreference matchPreference) {
        return matchPreferenceService.updateMatchPreferenceById(matchProfileId, preferenceId, matchPreference);
    }

    @DeleteMapping(path = "/matchProfile/{matchId}/matchPreference")
    public void deleteAllMatchPreferences(@PathVariable("matchId") Long matchProfileId) {
        matchPreferenceService.deleteMatchPreferences(matchProfileId);
    }

}
