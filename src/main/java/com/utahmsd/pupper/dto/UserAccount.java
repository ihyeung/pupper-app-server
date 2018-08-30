package com.utahmsd.pupper.dto;

import com.utahmsd.pupper.util.Utils;
import io.vavr.control.Try;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

//Entity representing a user's credential/account information

@Entity
@Table(name = "user_credential")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", updatable = false, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "computed_hash")
    private String computedHash; //store hash(password + salt)

    @Column(name = "salt")
    private String salt;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "token_issued")
    private Timestamp tokenIssued;

    @Column(name = "token_expires")
    private Timestamp tokenExpires;

    @Column(name = "is_valid")
    private boolean isValid;

    @Column(name = "join_date")
    private Date accountCreationDate;

    public UserAccount createAccountFromRequest(UserAuthenticationRequest request) {
        UserAccount userAccount = new UserAccount();
        userAccount.email = request.getEmail();
        userAccount.salt = request.getSalt();
        Try<byte[]> hash = Utils.computeHash(request.getPassword(), Try.of(()->salt.getBytes("UTF-8")).get());
        userAccount.computedHash = Base64.getEncoder().encodeToString(hash.get());
        userAccount.authToken = null; // TODO: implement this
        userAccount.accountCreationDate = Date.valueOf(LocalDate.now());
        return userAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getComputedHash() {
        return computedHash;
    }

    public void setComputedHash(String computedHash) {
        this.computedHash = computedHash;
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

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
}
