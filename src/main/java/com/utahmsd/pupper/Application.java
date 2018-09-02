package com.utahmsd.pupper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import com.utahmsd.pupper.util.Utils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}