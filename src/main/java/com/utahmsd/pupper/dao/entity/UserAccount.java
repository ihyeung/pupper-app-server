package com.utahmsd.pupper.dao.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Entity containing user credentials/account information.
 */
@Entity // reference to table defaults to table_name (i.e., reference database table MyTable using my_table)
@Table (name = "user_account", indexes = @Index(columnList = "username", name = "user_account_username_uindex"))
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Size(max = 50)
    @Email(message = "Username should be a valid email")
    private String username;

    @Size(max = 100)
    @NotBlank
    private String password;

    public UserAccount(){}

    static UserAccount createFromObject(Object object) {
        if (object != null) {
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;
            UserAccount userAccount = new UserAccount();
            userAccount.setId((Long) entityObject.get("id"));
            userAccount.setUsername((String) entityObject.get("username"));
            userAccount.setPassword((String) entityObject.get("password"));

            return userAccount;
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }
}