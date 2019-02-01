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

@RestController
public class MatchPreferenceController {

    private final MatchPreferenceService matchPreferenceService;

    @Autowired
    MatchPreferenceController(MatchPreferenceService matchPreferenceService) {
        this.matchPreferenceService = matchPreferenceService;
    }

    @PutMapping(path = "/matchProfile/{matchId}/matchPreference")
    public MatchPreferenceResponse insertMatchPreference(@PathVariable("matchId") Long matchProfileId,
                                                         @RequestBody @Valid MatchPreference matchPreference) {
        return matchPreferenceService.insertMatchPreference(matchProfileId, matchPreference);
    }
}
