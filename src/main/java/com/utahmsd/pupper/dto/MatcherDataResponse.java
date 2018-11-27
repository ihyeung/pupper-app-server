package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dto.pupper.ProfileCard;

import java.util.ArrayList;

public class MatcherDataResponse extends BaseResponse {

    @JsonProperty("playerId")
    private Long profileIdForPlayer;

    @JsonProperty("matcherData")
    private ArrayList<ProfileCard> nextMatcherDataBatch; //Next batch of un-viewed profile/matcher cards to display

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
