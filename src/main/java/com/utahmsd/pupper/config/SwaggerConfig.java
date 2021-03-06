package com.utahmsd.pupper.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ComponentScan(lazyInit = true)
//@EnableSwagger2
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

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pupper API")
                .license("MIT License: Copyright (C) 2018 Irene Yeung")
                .termsOfServiceUrl("Pending Publication")
                .description("Server/Back-End For Pupper")
                .version("v0.1.5.7")
                .build();
    }

}