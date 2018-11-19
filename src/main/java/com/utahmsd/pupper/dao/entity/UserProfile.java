package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_profile", indexes = @Index(columnList = "user_account_id_fk", name = "user_profile_ibfk_1"))
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id_fk")
    @Valid
    private UserAccount userAccount;

    @Size(min = 2, max = 30)
    @Column(name = "name_first")
    @NotBlank
    private String firstName;

    @Size(min = 1, max = 30)
    @Column(name = "name_last")
    private String lastName;

    @Column(name = "sex")
    @Size(min = 1, max = 1)
    private String sex; //M or F

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birthdate")
    @Past
    private Date birthdate; //For profile age

    @DefaultValue("single")
    @Column(name = "marital_status")
    @Size(min = 3, max = 20)
    private String maritalStatus;

    @Column(name = "zip")
    @Size(max = 5)
    private String zip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_join")
    @PastOrPresent
    private Date dateJoin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a")
    @Column(name = "last_login")
    @PastOrPresent
    private Date lastLogin;

    public UserProfile(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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

    public Date getDateJoin() { return dateJoin; }

    public void setDateJoin(Date dateJoin) { this.dateJoin = dateJoin; }

    public Date getLastLogin() { return lastLogin; }

    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
}
