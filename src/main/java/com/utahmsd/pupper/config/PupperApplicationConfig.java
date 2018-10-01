package com.utahmsd.pupper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@Configuration
@EnableJpaRepositories("com.utahmsd.pupper.dao")
public class PupperApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PupperApplicationConfig.class);

    @Value("${spring.datasource.driver-class-name:}")
    private String dbDriver;

    @Value("${spring.datasource.url:}")
    private String dbUrl;

//    @Value("${db.name}")
//    private String dbName;

//    @Value("${db.password}")
//    private String dbPassword;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("pupper")));

        return cacheManager;
    }

}
