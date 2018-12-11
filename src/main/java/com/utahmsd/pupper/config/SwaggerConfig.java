package com.utahmsd.pupper.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ComponentScan(lazyInit = true)
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build().pathMapping("/")
                .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    @Bean
    public Docket healthDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("health-api")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/sla.*"))
                .build();
    }

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
                .license("Copyright (C) 2018 Irene Yeung")
                .termsOfServiceUrl("Pending Publication")
                .description("RESTful Web Application For Pupper Mobile App")
                .version("0.1.0")
                .build();
    }

}