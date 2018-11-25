package com.utahmsd.pupper.dao.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Entity containing user credentials/account information.
 */
@Entity // reference to table defaults to table_name (i.e., reference database table MyTable using my_table)
@Table (indexes = @Index(columnList = "username", name = "user_account_username_uindex"))
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Size(max = 50)
    @Email(message = "Username should be a valid email")
    private String username;

    @Size(max = 100)
    @NotBlank
    private String password;
//
//    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    private UserProfile userProfile;

    public UserAccount(){}

    public UserAccount(String username) {
        this.username = username;
        this.password = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public UserProfile getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(UserProfile userProfile) {
//        this.userProfile = userProfile;
//    }
}