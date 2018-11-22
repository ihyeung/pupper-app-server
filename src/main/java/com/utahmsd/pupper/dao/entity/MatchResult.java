package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity containing MatchResult data generated by the PupperMatcherService.
 */

@Entity
@Table(name = "match_result",
        indexes = {@Index(columnList = "match_profile_id_fk_1", name = "match_result_ibfk_1"),
        @Index(columnList = "match_profile_id_fk_2", name = "match_result_ibfk_2")})
public class MatchResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //A match result corresponds to multiple (2) match profiles, and a match profile has many match results
    @JoinColumn(name = "match_profile_id_fk_1")
    @Valid
    private MatchProfile matchProfileOne;

    @Column(name = "match_profile_1_result")
    @DefaultValue("false")
    private boolean isMatchForProfileOne; //Match profile 1's decision on match profile 2

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_profile_id_fk_2")
    @Valid
    private MatchProfile matchProfileTwo;

    @Column(name = "match_profile_2_result")
    @DefaultValue("false")
    private boolean isMatchForProfileTwo; //Match profile 2's decision on match profile 1

    @Column(name = "last_update_match_result")
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a")
    private Date lastUpdateToMatchResult; //Date that match result was last updated

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public MatchProfile getMatchProfileOne() { return matchProfileOne; }

    public void setMatchProfileOne(MatchProfile matchProfileOne) { this.matchProfileOne = matchProfileOne; }

    public boolean isMatchForProfileOne() { return isMatchForProfileOne; }

    public void setMatchForProfileOne(boolean matchForProfileOne) { isMatchForProfileOne = matchForProfileOne; }

    public MatchProfile getMatchProfileTwo() { return matchProfileTwo; }

    public void setMatchProfileTwo(MatchProfile matchProfileTwo) { this.matchProfileTwo = matchProfileTwo; }

    public boolean isMatchForProfileTwo() { return isMatchForProfileTwo; }

    public void setMatchForProfileTwo(boolean matchForProfileTwo) { isMatchForProfileTwo = matchForProfileTwo; }

    public Date getLastUpdateToMatchResult() { return lastUpdateToMatchResult; }

    public void setLastUpdateToMatchResult(Date lastUpdateToMatchResult) { this.lastUpdateToMatchResult = lastUpdateToMatchResult; }

}
