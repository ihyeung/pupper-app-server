package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utahmsd.pupper.dto.pupper.Gender;
import com.utahmsd.pupper.dto.pupper.MaritalStatus;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.utahmsd.pupper.util.Constants.DATE_FORMAT;
import static com.utahmsd.pupper.util.Constants.DATE_FORMATTER;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    @NotBlank
    private Gender sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @Column(name = "birthdate")
    @Past
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "zip")
    @Size(max = 5)
    private String zip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @Column(name = "date_join")
    @PastOrPresent
    private Date dateJoin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @Column(name = "last_login")
    @PastOrPresent
    private Date lastLogin;

    @Column(name = "profile_image")
    @javax.validation.constraints.Size(max = 100)
    private String profileImage;

    public UserProfile(){}

    public static UserProfile createFromObject(Object object) throws ParseException {
        if (object != null) {
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;

            UserProfile userProfile = new UserProfile();
            userProfile.setId((Long) entityObject.get("id"));
            userProfile.setUserAccount(UserAccount.createFromObject(entityObject.get("userAccount")));
            userProfile.setFirstName((String) entityObject.get("firstName"));
            userProfile.setLastName((String) entityObject.get("lastName"));
            userProfile.setSex(Gender.valueOf((String) entityObject.get("sex")));
            userProfile.setBirthdate(DATE_FORMATTER.parse((String) entityObject.get("birthdate")));
            userProfile.setMaritalStatus(MaritalStatus.valueOf((String) entityObject.get("maritalStatus")));
            userProfile.setZip((String) entityObject.get("zip"));
            userProfile.setDateJoin(DATE_FORMATTER.parse((String) entityObject.get("dateJoin")));
            userProfile.setLastLogin(DATE_FORMATTER.parse((String) entityObject.get("lastLogin")));
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

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
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
