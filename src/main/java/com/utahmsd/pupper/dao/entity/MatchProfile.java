package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dao.entity.Breed;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

//Entity representing each individual match profile that a given user has (for 1-3 dogs)

@Entity
@Table(name = "match_profile")
public class MatchProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A user can have multiple match profiles
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @Column(name = "breed")
    private Breed breed;

    @Column(name = "pupper_score")
    private float score;

    @Column(name = "about")
    private String aboutMe;
}
