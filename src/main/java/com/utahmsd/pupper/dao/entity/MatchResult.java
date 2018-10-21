package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dao.entity.MatchProfile;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity containing all PupperMatcherService data
 */

@Entity
@Table(name = "match_result")
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A match result corresponds to multiple (2) match profiles, and a match profile has many match results
    @JoinColumn(name = "match_profile_id", insertable = false, updatable = false)
    private MatchProfile matchProfileOne;

    @Column(name = "match_profile_1_result")
    private boolean isMatchForProfileOne; //Match profile 1's decision on match profile 2

    @ManyToOne
    @JoinColumn(name = "match_profile_id", insertable = false, updatable = false)
    private MatchProfile matchProfileTwo;

    @Column(name = "match_profile_2_result")
    private boolean isMatchForProfileTwo; //Match profile 2's decision on match profile 1

    @Column(name = "last_update_match_result")
    private Date lastUpdateToMatchResult; //Date that match result was last updated

}
