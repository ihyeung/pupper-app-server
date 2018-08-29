package com.utahmsd.pupper.dto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "name")
    private String name;

    @Column(name = "userId")
    private Long accountId;

    @Column(name = "dob")
    private Date dob; //For profile age

    @Column(name = "location")
    private String location; //May change to zip?

    @Column(name = "sex")
    private char sex;

    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "score")
    private Float score;

    @Column(name = "profileId") //UserProfile object representing a user's bio?
    private Long profileId;

    //How to handle user profile image?

    public User() {
    }
}
