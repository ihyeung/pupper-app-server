package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.UserAuthenticationRequest;

import javax.persistence.*;
import java.io.Serializable;

//Entity representing a user's credential/account information

//Keep only fields that will not be modified/updated frequently

@Entity
@Table(name = "UserCredentials")
public class UserCredentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "computed_hash")
    private String computedHash; //store hash(password + salt)

    @Column(name = "salt")
    private String salt;

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
}
