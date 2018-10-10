package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.UserProfileRequest;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
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

    @Size(min = 2, max = 30)
    @Column(name = "name_first")
    private String firstName;

    @Size(min = 1, max = 30)
    @Column(name = "name_last")
    private String lastName;

    @Max(1)
    @Column(name = "sex")
    private char sex; //M or F

    @Column(name = "birthdate")
    private Date birthdate; //For profile age

    @DefaultValue("Single")
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
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
