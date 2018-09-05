package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.UserProfileRequest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "sex")
    private char sex;

    @Column(name = "dob")
    private Date dob; //For profile age

    @Column(name = "location")
    private String location; //May change to zip?


    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "profileId") //UserProfile object representing a user's bio?
    private Long profileId;

    @Column(name = "image")
    private String image; //String path to image location?
    //How to handle user profile image?

    @Column(name = "pupper_score")
    private float pupperScore;

    public UserProfile(){}

    public UserProfile(UserProfileRequest request) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getPupperScore() {
        return pupperScore;
    }

    public void setPupperScore(float pupperScore) {
        this.pupperScore = pupperScore;
    }
}
