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
                                                         @RequestBody @Valid List<MatchPreference> matchPreference) {
        return matchPreferenceService.insertMatchPreferences(matchProfileId, matchPreference);
    }

    @GetMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse getMatchPreferences(@PathVariable("matchId") Long matchProfileId) {
        return null;
    }

    @PutMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse updateMatchPreferences(@PathVariable("matchId") Long matchProfileId,
                                                       @RequestBody @Valid List<MatchPreference> matchPreference) {
        return null;
    }

    @DeleteMapping(path = "/matchProfile/{matchId}/matchPreference")
    public void deleteAllMatchPreferences(@PathVariable("matchId") Long matchProfileId) {

    }


}
