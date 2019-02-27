package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static com.utahmsd.pupper.dto.pupper.ProfileCard.matchProfileToProfileCardMapper;


public class MatcherDataResponse extends BaseResponse {

    @JsonProperty("profileId")
    private Long matchProfileId;

    @JsonProperty("matchProfiles")
    private ArrayList<ProfileCard> matchProfileBatch; //Next batch of un-viewed profile/matcher cards to display

    public static MatcherDataResponse createMatcherDataResponse(boolean success, List<MatchProfile> matchProfiles,
                                                                HttpStatus code, String description) {
        MatcherDataResponse response = new MatcherDataResponse();
        response.setSuccess(success);
        response.setMatchProfileBatch(matchProfiles == null ? new ArrayList<>() : matchProfileToProfileCardMapper(matchProfiles));
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public Long getMatchProfileId() {
        return matchProfileId;
    }

    public void setMatchProfileId(Long matchProfileId) {
        this.matchProfileId = matchProfileId;
    }

    public ArrayList<ProfileCard> getMatchProfileBatch() {
        return matchProfileBatch;
    }

    public void setMatchProfileBatch(ArrayList<ProfileCard> matchProfileBatch) {
        this.matchProfileBatch = matchProfileBatch;
    }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code, String description) {
        return null;
    }
}
