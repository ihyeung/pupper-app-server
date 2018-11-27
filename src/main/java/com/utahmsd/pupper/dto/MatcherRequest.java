package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;

import java.util.Date;

/**
 * DTO representing a single request that will sent when a user is using the PupperMatcher using a given (selected)
 * matching profile and clicks either yes or no button
 * for another matching profile.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MatcherRequest {

    @JsonProperty("matchingProfile")
    private MatchProfile matchProfile; //The match profile of the user who is doing the swiping/clicking

    @JsonProperty("pupperMatch")
    private MatchProfile pupperMatch; //The match profile being shown to the user

    @JsonProperty("isLike")
    private boolean isLike; // Whether the yes/like button was clicked by the user

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }

    public MatchProfile getPupperMatch() {
        return pupperMatch;
    }

    public void setPupperMatch(MatchProfile pupperMatch) {
        this.pupperMatch = pupperMatch;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
