package com.utahmsd.pupper.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyJWTs";
    public static final long AUTH_TOKEN_EXPIRATION = 86_400_000L; // Access token is valid for 24 hours
    public static final String ACCESS_TOKEN_TYPE = "Bearer "; // eg. 'Bearer VNxetTDTldXNV'
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String REGISTER_ENDPOINT = "/auth/register";
    public static final String[] SWAGGER_WHITELIST = {"/api/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs"};

}
