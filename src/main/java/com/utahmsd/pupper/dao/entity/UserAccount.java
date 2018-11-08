package com.utahmsd.pupper.dao.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity containing user credentials/account information.
 */
@Entity // reference to table defaults to table_name (i.e., reference database table MyTable using my_table)
@Table (indexes = @Index(columnList = "username", name = "user_account_username_uindex"))
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    @Email(message = "Username should be a valid email")
    private String username;

    @Size(max = 100)
    @NotBlank
    private String password;

    public UserAccount(){}

    public UserAccount(String username) {
        this.username = username;
        this.password = null;
    }

    public long getId() {
        return id;
    }

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
}