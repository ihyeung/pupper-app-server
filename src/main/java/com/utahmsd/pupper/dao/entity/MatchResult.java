package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity containing all PupperMatcherService data
 */

@Entity
@Table(name = "match_result")
public class MatchResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A match result corresponds to multiple (2) match profiles, and a match profile has many match results
    @JoinColumn(name = "match_profile_id", insertable = false, updatable = false)
    @Valid
    private MatchProfile matchProfileOne;

    @Column(name = "match_profile_1_result")
    private boolean isMatchForProfileOne; //Match profile 1's decision on match profile 2

    @ManyToOne
    @JoinColumn(name = "match_profile_id", insertable = false, updatable = false)
    @Valid
    private MatchProfile matchProfileTwo;

    @Column(name = "match_profile_2_result")
    private boolean isMatchForProfileTwo; //Match profile 2's decision on match profile 1

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "last_update_match_result")
    @PastOrPresent
    private Date lastUpdateToMatchResult; //Date that match result was last updated

}
