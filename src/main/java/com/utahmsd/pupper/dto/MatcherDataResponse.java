package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.pupper.ProfileCard;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static com.utahmsd.pupper.dto.pupper.ProfileCard.matchProfileToProfileCardMapper;


//TODO: Rename these fields
public class MatcherDataResponse extends BaseResponse {

    @JsonProperty("playerId")
    private Long profileIdForPlayer;

    @JsonProperty("matcherData")
    private ArrayList<ProfileCard> nextMatcherDataBatch; //Next batch of un-viewed profile/matcher cards to display

    public static MatcherDataResponse createMatcherDataResponse(boolean success, List<MatchProfile> matchProfiles,
                                                                HttpStatus code, String description) {
        MatcherDataResponse response = new MatcherDataResponse();
        response.setSuccess(success);
        response.setNextMatcherDataBatch(matchProfiles == null ? new ArrayList<>() : matchProfileToProfileCardMapper(matchProfiles));
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public Long getProfileIdForPlayer() {
        return profileIdForPlayer;
    }

    public void setProfileIdForPlayer(Long profileIdForPlayer) {
        this.profileIdForPlayer = profileIdForPlayer;
    }

    public ArrayList<ProfileCard> getNextMatcherDataBatch() {
        return nextMatcherDataBatch;
    }

    public void setNextMatcherDataBatch(ArrayList<ProfileCard> nextMatcherDataBatch) {
        this.nextMatcherDataBatch = nextMatcherDataBatch;
    }
}
