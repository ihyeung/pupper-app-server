package com.utahmsd.pupper.security;

class SecurityConstants {
    static final String SECRET = "SecretKeyJWTs";
    static final long AUTH_TOKEN_EXPIRATION = 86_400_000L; // Access token is valid for 24 hours
//    static final long AUTH_TOKEN_EXPIRATION = 30_000L; //Make token expire after 30 seconds for testing

    static final String ACCESS_TOKEN_TYPE = "Bearer "; // eg. 'Bearer VNxetTDTldXNV'
    static final String AUTH_HEADER_KEY = "Authorization";
    static final String REGISTER_ENDPOINT = "/account/register";
    static final String[] SWAGGER_WHITELIST =
            {"/api/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/docs/**", "/webjars/**"};
}
