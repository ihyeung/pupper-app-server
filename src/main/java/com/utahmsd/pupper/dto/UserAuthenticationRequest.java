package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthenticationRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("signingString")
    private String signingString;

    @JsonProperty("registerUser")
    private boolean registerUser; //flag for requests to register new user account

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

    public String getSigningString() {
        return signingString;
    }

    public void setSigningString(String signingString) {
        this.signingString = signingString;
    }

    public boolean isRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(boolean registerUser) {
        this.registerUser = registerUser;
    }
}
