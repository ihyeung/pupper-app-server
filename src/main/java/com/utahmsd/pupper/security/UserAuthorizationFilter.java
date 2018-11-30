package com.utahmsd.pupper.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.utahmsd.pupper.security.SecurityConstants.*;

public class UserAuthorizationFilter extends BasicAuthenticationFilter {

    UserAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //Check for valid access token 'Authorization Bearer:' field
        String header = request.getHeader(AUTH_HEADER_KEY);

        if (header == null || !header.startsWith(ACCESS_TOKEN_TYPE)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = validateAuthToken(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken validateAuthToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_HEADER_KEY).replace(ACCESS_TOKEN_TYPE, "");
        if (!accessToken.isEmpty()) { //If Authorization Bearer field exists, verify the access token
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(accessToken)
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
