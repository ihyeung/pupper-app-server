package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.UserProfileRequest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema="u0934995", name = "user_profile")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_credentials_id")
    private UserCredentials userCredentials;

    @Column(name = "name_first")
    private String firstName;

    @Column(name = "name_last")
    private String lastName;

    @Column(name = "sex")
    private char sex;

    @Column(name = "birthdate")
    private Date birthdate; //For profile age

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "zip")
    private String zip;

    @Column(name = "last_login")
    private Date lastLogin;

    public UserProfile(){}

    public UserProfile(UserProfileRequest request) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentialsId) {
        this.userCredentials = userCredentialsId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Date getLastLogin() { return lastLogin; }

    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
}
