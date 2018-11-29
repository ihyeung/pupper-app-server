package com.utahmsd.pupper.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.dao.entity.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.utahmsd.pupper.security.SecurityConstants.*;

/**
 * Filter class for authenticating users that generates a JWT upon success.
 */

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationFilter.class);
    private Date sessionAuthSuccess;
    private Date sessionAuthExpires;

    private AuthenticationManager authenticationManager;

    UserAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserAccount creds = new ObjectMapper()
                    .readValue(request.getInputStream(), UserAccount.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth){

        long expiresIn = AUTH_TOKEN_EXPIRATION;
        Date currentTime = Date.from(Instant.now());
        if (sessionAuthSuccess != null) { //If a previously authenticated session, update value of expiresIn header

            expiresIn -= (currentTime.getTime() - sessionAuthSuccess.getTime());

            if (expiresIn > 0) {
                LOGGER.info("Access token is valid and will expire in {} ms.", expiresIn);
            } else {
                LOGGER.error("Access token is expired.");
                expiresIn = AUTH_TOKEN_EXPIRATION; //Reset expiresInMilliseconds value
                sessionAuthSuccess = null; //Reset auth session timestamp
                sessionAuthExpires = null;
            }
        }

        if (sessionAuthSuccess == null) { //If a new session is established, set session timestamp
            sessionAuthSuccess = currentTime;
            sessionAuthExpires = new Date(currentTime.getTime() + AUTH_TOKEN_EXPIRATION);
        }

        //Upon successful authentication, either creates new JWT or references previously issued JWT
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(sessionAuthExpires)
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(AUTH_HEADER_KEY, ACCESS_TOKEN_TYPE + token);
        response.setHeader("Expires", sessionAuthExpires.toString()); //Timestamp of when it expires
        response.setHeader("expiresInMs", Long.toString(expiresIn)); //Remaining ms left


    }
}
