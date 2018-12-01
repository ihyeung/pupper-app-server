package com.utahmsd.pupper.acceptance;

import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserProfileControllerAT {

    private static final String TEST_URL = System.getProperty("TEST_URL", "http://localhost:5000");

    private static UserAccount userAccount= new UserAccount();
    private static String accessToken = null;

    @Before
    public void init(){
        RestAssured.baseURI = TEST_URL;
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        userAccount.setUsername("createUserProfileTest@test.com");
        userAccount.setPassword("TESTPASSWORD");
        userAccount.setId(3L);

        if (accessToken == null) {
            accessToken =
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
                    extract().response().header("Authorization");
        }
    }

    @Test
    public void testGetUserProfiles() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
                param("limit", 1).
                param("sortBy", "lastName").
        when().
                get("/user").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    @Test
    public void testGetUserProfileByEmail() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
                param("email", "createUserProfileTest@test.com").
        when().
                get("/user").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    @Test
    public void testGetUserProfileById() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
        when().
                get("/user/1").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    @Test
    public void testUpdateUserProfileByAccountEmail() {

        UserProfile profileToUpdate = createUserProfile();
        profileToUpdate.setId(3L);
        profileToUpdate.getUserAccount(). //When doing updateUserProfile, don't want to modify hash of userAccount password in the process
                setPassword("$2a$04$vb9A83WYIyo1Ny7qFI8V3.AbKWZ2bz1G14kzJNj5d6bI1wbp47siu");

        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(profileToUpdate).
                header(new Header("Authorization", accessToken)).
                param("email", userAccount.getUsername()).
        when().
                put("/user").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true));
    }

    @Test
    public void testUpdateUserProfileLastLogin() {

        String lastLogin = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
                param("lastLogin", lastLogin).
        when().
                put("/user/3").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true));
    }

    @Test
    public void testCreateOrUpdateUserProfile() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(createUserProfile()).
                header(new Header("Authorization", accessToken)).
        when().
                post("/user").
        then().
                log().all().
                statusCode(200).
                body("isSuccess", equalTo(true)).
                body("userProfiles", notNullValue());
    }

    private UserProfile createUserProfile() {
        UserProfile user = new UserProfile();
        user.setUserAccount(userAccount);

        user.setFirstName("Carmen");
        user.setLastName("San Diego");
        user.setBirthdate(Date.from(Instant.parse("2004-12-03T10:15:30.00Z")));
        user.setZip("84095");
        user.setSex("F");
        user.setMaritalStatus("Single");
        user.setLastLogin(Date.from(Instant.now()));
        user.setDateJoin(Date.from(Instant.now()));

        return user;
    }

}
