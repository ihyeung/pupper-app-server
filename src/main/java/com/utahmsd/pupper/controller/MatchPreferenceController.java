package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.dao.entity.MatchPreference;
import com.utahmsd.pupper.dto.MatchPreferenceResponse;
import com.utahmsd.pupper.service.MatchPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MatchPreferenceController {

    private final MatchPreferenceService matchPreferenceService;

    @Autowired
    MatchPreferenceController(MatchPreferenceService matchPreferenceService) {
        this.matchPreferenceService = matchPreferenceService;
    }

    @PutMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse insertMatchPreferences(@PathVariable("matchId") Long matchProfileId,
                                                         @RequestBody @Valid List<MatchPreference> matchPreference) {
        return matchPreferenceService.insertMatchPreferences(matchProfileId, matchPreference);
    }
}
