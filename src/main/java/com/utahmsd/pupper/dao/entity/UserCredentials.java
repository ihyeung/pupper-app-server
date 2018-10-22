package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utahmsd.pupper.dto.User;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity representing a user's credential/account information.
 * Only stores user data that will be modified infrequently.
 */


@Entity
@Table(name = "user_credentials")
public class UserCredentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

//    @NotNull
//    @Size(min = 10, max = 40)
//    @Column(name = "email")
    private String email;
//
//    @Column(name = "password")
    private String password;

    @Valid
    @Transient
    private User user;

    @Column(name = "hash")
    private String computedHash; //store hash(password + salt)

    @Column(name = "salt")
    private String salt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_joined")
    private Date dateJoined;

    public UserCredentials(){}

    public UserCredentials(UserAuthenticationRequest request) {
//        email = request.getEmail();
//        salt = request.getSalt();
//        Try<byte[]> hash = Utils.computeHash(request.getPassword(), Try.of(()->salt.getBytes("UTF-8")).get());
//        computedHash = Base64.getEncoder().encodeToString(hash.get());
//        authToken = null; // TODO: implement this
//        accountCreationDate = Date.valueOf(LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        if (user == null) {
            user = new User(email, password);
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


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

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
}
