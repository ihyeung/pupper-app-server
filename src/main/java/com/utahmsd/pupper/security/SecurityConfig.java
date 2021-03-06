package com.utahmsd.pupper.security;

import com.utahmsd.pupper.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.utahmsd.pupper.security.SecurityConstants.REGISTER_ENDPOINT;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${pupper.mobile.application.path:}")
    private String appPathDevice;


    private final UserAccountService userAccountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserAccountService userAccountService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userAccountService = userAccountService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
//        authenticationMgr.inMemoryAuthentication()
//                .withUser(userName).password(userPassword).authorities(userRole);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

//        http.requiresChannel().anyRequest().requiresSecure();//TODO: figure out how to enable https for backend calls from front end

        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, SWAGGER_WHITELIST).permitAll() //Uncomment for swagger2markup tests
                .antMatchers(HttpMethod.POST, REGISTER_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()

        .addFilter(new UserAuthenticationFilter(authenticationManager()))
                .addFilter(new UserAuthorizationFilter(authenticationManager()))
                // Disable session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAccountService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);

        corsConfiguration.addAllowedOrigin(appPathDevice);

        corsConfiguration.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**",
                corsConfiguration);
        return source;
    }
}
