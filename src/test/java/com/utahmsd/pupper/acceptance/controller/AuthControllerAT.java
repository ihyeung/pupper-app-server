package com.utahmsd.pupper.acceptance.controller;

import com.utahmsd.pupper.dao.entity.UserAccount;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class AuthControllerAT extends BaseAcceptanceTest {

    private static final String TEST_URL = System.getProperty("TEST_URL", "http://localhost:5000");

    @Before
    public void init(){
        RestAssured.baseURI = TEST_URL;
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

    }

    @Ignore
    @Test
    public void testCreateNewUserAccount_success() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(createNewTempUserAccount()).
        when().
                post("/account/register").
        then().
                log().all().
                statusCode(200).
                body(notNullValue()).
                body("isSuccess", equalTo(true)).
                body("userAccounts", notNullValue());
    }

    @Test
    public void testCreateNewUserAccount_existingUserAccount_returnsConflict() {

        UserAccount userAccount = createNewTempUserAccount();
        userAccount.setUsername("c0e57745-b607-4133-804f-114c54b74d54@test.com");

        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(userAccount).
        when().
                post("/account/register").
        then().
                log().all().
                statusCode(409).
                body(notNullValue()).
                body("isSuccess", equalTo(false));
    }

    @Test
    public void testUserLogin_success() {

        UserAccount userAccount = createNewTempUserAccount();
        userAccount.setUsername("c0e57745-b607-4133-804f-114c54b74d54@test.com");

        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(userAccount).
        when().
                post("/login").
        then().
                log().all().
                statusCode(200).
                body(notNullValue()).
                header("Authorization", notNullValue());
    }

    public static UserAccount createNewTempUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(UUID.randomUUID() + "@test.com");
        userAccount.setPassword("PASSWORD");

        return userAccount;
    }
}
