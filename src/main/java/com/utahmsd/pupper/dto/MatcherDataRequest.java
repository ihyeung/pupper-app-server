package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

/**
 * DTO similar to MatcherRequest, except request contains a hashmap of results to perform database operations
 * as a batch for a given playerId.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MatcherDataRequest {

    @JsonProperty("matchProfileId")
    private Long matchProfileId;

    @JsonProperty("matcherResults")
    private Map<Long, Boolean> map;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a")
    private Date date;

    public Long getMatchProfileId() { return matchProfileId; }

    public void setMatchProfileId(Long matchProfileId) {this.matchProfileId = matchProfileId;}

    public Map<Long, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<Long, Boolean> map) {
        this.map = map;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
