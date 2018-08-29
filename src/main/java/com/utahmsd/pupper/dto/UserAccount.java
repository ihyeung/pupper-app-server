package com.utahmsd.pupper.dto;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "userAccount")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "authToken")
    private String authToken;

    @Column(name = "tokenIssued")
    private Timestamp tokenIssued;

    @Column(name = "tokenExpires")
    private Timestamp tokenExpires;

    @Column(name = "isValidToken")
    private boolean isValid;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Timestamp getTokenIssued() {
        return tokenIssued;
    }

    public void setTokenIssued(Timestamp tokenIssued) {
        this.tokenIssued = tokenIssued;
    }

    public Timestamp getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(Timestamp tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
