package com.utahmsd.pupper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PupperApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("pupper")));
//
//        return cacheManager;
//    }


//    @PersistenceUnit
//    EntityManagerFactory entityManagerFactory;
//
//    @PersistenceContext
//    EntityManager entityManager = entityManagerFactory.createEntityManager();

}
