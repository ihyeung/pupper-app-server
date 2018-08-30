package com.utahmsd.pupper.dto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

//Entity repesenting each individual pupper's separate info (

@Entity
@Table(name = "pupper")
public class Pupper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pupper_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age")
    private String age;

    @Column(name = "life_stage")
    private LifeStage lifeStage;

    @Column(name = "breed")
    private String breed;

    @ManyToOne //Multiple puppers can reference a single MatchProfile
    @JoinColumn(name = "match_profile_id") //foreign key referencing 'id' column in MatchProfile entity
    private Long matchProfileId;

    @ManyToOne //Many puppers can point to userid
    @JoinColumn(name = "user_id") //foreign key that references 'id' column in User entity
    private User userId;


    //Access pupper location and last login by referencing user table
    //Individual pupper doesnt store bio, this is stored in MatchProfile entity

    //How to store pupper profile images?

}
