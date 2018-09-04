//package com.utahmsd.pupper.dto;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//
////Entity representing each individual match profile that a given user has (for 1-3 dogs)
//
//@Entity
//@Table(name = "matcher_profile")
//public class MatchProfile {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "match_profile_id", updatable = false, nullable = false)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Long userId;
//
//    @Column(name = "num_dogs_in_profile")
//    private Long numDogs;
//
//
//    // Up to 3 dogs can be added to a matching profile
//    @OneToMany // One match profile can reference many puppers
//    @JoinColumn(name = "pupper_id") //PupperProfile #1
//    private Long pupId;
//
//    @OneToMany
//    @JoinColumn(name = "pupper_id")
//    private Long pupId2;
//
//    @OneToMany
//    @JoinColumn(name = "pupper_id")
//    private Long pupId3;
//
//    @OneToOne
//    @JoinColumn(name = "last_login")
//    private Timestamp lastLogin;
//
//    @Column(name = "pupper_score")
//    private float score;
//
//    @Column(name = "about")
//    private String aboutMe;
//}
