package com.utahmsd.pupper.config;
//

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.dao.Pupper;
import com.utahmsd.pupper.dao.UserProfile;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServer;
//import org.springframework.boot.web.servlet.ServletContextInitializer;
//import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.concurrent.ConcurrentMapCache;
//import org.springframework.cache.support.SimpleCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Arrays;
//
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SpringApplicationConfig {

//    @PersistenceUnit
//    EntityManagerFactory entityManagerFactory;
//
//    @PersistenceContext
//    EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build().pathMapping("/")
                .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }
//    @Bean
//    protected ResourceHandlerRegistry addResourceHandlers() {
//        ResourceHandlerRegistry registry = new ResourceHandlerRegistry();
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

    //
//    @Bean
//    ServletWebServerFactory servletWebServerFactory(){
//        return new TomcatServletWebServerFactory();
//    }
//
//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("pupper")));
//
//        return cacheManager;
//    }
////
//    @Bean
//    public Docket healthDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("health-api")
//                .apiInfo(apiInfo())
//                .select()
//                .paths(regex("/sla.*"))
//                .build();
//    }

    @Bean
    public Docket pupperDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("pupper-api")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/pupper.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pupper API")
                .license("(C) Copyright Kayla/Irene")
                .description("Pupper Mobile App")
                .version("2.0")
                .build();
    }

}