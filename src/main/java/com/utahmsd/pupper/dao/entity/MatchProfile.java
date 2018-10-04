package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dao.entity.Breed;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

//Entity representing each individual match profile that a given user has (for 1-3 dogs)

@Entity
@Table(name = "MatchingProfile")
public class MatchProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_profile_id", updatable = false, nullable = false)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "user_profileId")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "breed")
    private Breed breed;
//    @Column(name = "num_dogs_in_profile")
//    private int numDogs;

    // Up to 3 dogs can be added to a matching profile
//    @OneToMany // One match profile can reference many puppers
    @Column(name = "pup_id")
//    @JoinColumn(name = "pupper_id") //PupperProfile #1
    private Long pupId;

//    @OneToMany
//    @JoinColumn(name = "pupper_id")
//    private Long pupId2;
//
//    @OneToMany
//    @JoinColumn(name = "pupper_id")
//    private Long pupId3;

//    @OneToOne
//    @JoinColumn(name = "last_login")
    private Date lastLogin;

    @Column(name = "pupper_score")
    private float score;

    @Column(name = "about")
    private String aboutMe;
}
