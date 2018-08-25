package com.utahmsd.pupper.dto;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "pupper")
public class Pupper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User owner;

    @JoinColumn(name = "user_location")
    private String location;

    @JoinColumn(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "about")
    private String aboutMe;

    @Column(name = "pupper_index")
    private Double pupIndexScore;

}
