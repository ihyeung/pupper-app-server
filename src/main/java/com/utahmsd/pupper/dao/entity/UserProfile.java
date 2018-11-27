package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

@Entity
@Table(name = "user_profile", indexes = @Index(columnList = "user_account_id_fk", name = "user_profile_ibfk_1"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "last_login")
    @PastOrPresent
    private Date lastLogin;

    @Column(name = "profile_image")
    @javax.validation.constraints.Size(max = 100)
    private String profileImage;

    public UserProfile(){}

    static UserProfile createFromObject(Object object) throws ParseException {
        if (object != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;

            UserProfile userProfile = new UserProfile();
            userProfile.setId((Long) entityObject.get("id"));
            userProfile.setUserAccount(UserAccount.createFromObject(entityObject.get("userAccount")));
            userProfile.setFirstName((String) entityObject.get("firstName"));
            userProfile.setLastName((String) entityObject.get("lastName"));
            userProfile.setSex((String) entityObject.get("sex"));
            userProfile.setBirthdate(dateFormat.parse((String) entityObject.get("birthdate")));
            userProfile.setMaritalStatus((String) entityObject.get("maritalStatus"));
            userProfile.setZip((String) entityObject.get("zip"));
            userProfile.setDateJoin(dateFormat.parse((String) entityObject.get("dateJoin")));
            userProfile.setLastLogin(dateFormat.parse((String) entityObject.get("lastLogin")));
            userProfile.setProfileImage((String) entityObject.get("profileImage"));

            return userProfile;
        }
        return null;
    }

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
