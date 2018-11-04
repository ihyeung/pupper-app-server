package com.utahmsd.pupper.security;

class SecurityConstants {
    static final String SECRET = "SecretKeyJWTs";
    static final long AUTH_TOKEN_EXPIRATION = 86_400_000L; // Access token is valid for 24 hours
    static final String ACCESS_TOKEN_TYPE = "Bearer "; // eg. 'Bearer VNxetTDTldXNV'
    static final String AUTH_HEADER_KEY = "Authorization";
    static final String REGISTER_ENDPOINT = "/auth/register";
    static final String[] SWAGGER_WHITELIST =
            {"/api/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/docs/**"};
}
