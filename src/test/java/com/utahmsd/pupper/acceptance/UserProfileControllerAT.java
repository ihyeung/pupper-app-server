package com.utahmsd.pupper.acceptance;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserProfileControllerAT {

    private static final String TEST_URL = System.getProperty("TEST_URL", "http://localhost:9000/");

    private static UserAccount userAccount= new UserAccount();
    private static String accessToken = null;

    @Before
    public void init(){
        RestAssured.baseURI = TEST_URL;
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        userAccount.setUsername("createUserProfileTest@test.com");
        userAccount.setPassword("TESTPASSWORD");

        if (accessToken == null) {
            accessToken =given().
                    relaxedHTTPSValidation().
                    log().all().
                    contentType(ContentType.JSON).
                    body(userAccount).
                    when().
                    post("/login").
                    then().
                    log().all().
                    statusCode(200).
                    body("isSuccess", equalTo(true)).
                    extract().response().header("Authorization");
        }
        System.out.println("Access token: " + accessToken);

    }

    @Test
    public void testGetAllUsers() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
        when().
                get("/user/").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    @Test
    public void testFindUserById() {

    }

    @Test
    public void testUpdateUserProfile() {

    }


    @Test
    public void testCreateUserProfile() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(createUserProfile()).
                when().
                post("/user").
                then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    @Test
    public void testDeleteUserProfileById() {

    }

    private UserProfile createUserProfile() {
        UserProfile user = new UserProfile();
        user.setUserAccount(userAccount);
        user.setFirstName("Carmen");
        user.setLastName("San Diego");
        user.setBirthdate(Date.from(Instant.parse("2004-12-03T10:15:30.00Z")));
        user.setZip("84095");
        user.setSex('F');
        user.setLastLogin(Date.from(Instant.now()));

        return user;
    }

}
