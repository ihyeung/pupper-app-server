package com.utahmsd.pupper.dto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long Id;

    @Column(name = "name")
    private String firstName;

    @Column(name = "sex")
    private char sex;

    @Column(name = "dob")
    private Date dob; //For profile age

    @Column(name = "location")
    private String location; //May change to zip?


    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "last_login")
    private Timestamp lastLogin;




    @Column(name = "profileId") //UserProfile object representing a user's bio?
    private Long profileId;

    //How to handle user profile image?

    public User() {
    }
}
