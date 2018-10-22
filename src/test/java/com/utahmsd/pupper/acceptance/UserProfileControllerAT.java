package com.utahmsd.pupper.acceptance;

import com.utahmsd.pupper.dao.entity.UserCredentials;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.User;
import com.utahmsd.pupper.dto.UserProfileRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProfileControllerAT {

    private final String TEST_URL = System.getProperty("TEST_URL");

    @Before
    public void setup(){
        RestAssured.baseURI = TEST_URL;
    }

    @Test
    public void testFindAllUsers() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
        when().
                get("/users/").
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
        UserProfileRequest request = new UserProfileRequest();
        request.setRequestingUserId(Long.parseLong("2"));
        request.setUserProfile(createUserProfile());

        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                body(request).
        when().
                post("/users/").
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
        UserCredentials credentials = new UserCredentials();
        User userLogin = new User("createUserProfileTest@test.com", "TEST");
        credentials.setUser(userLogin);
        credentials.setSalt("SALT");
        credentials.setDateJoined(Date.from(Instant.now()));

        UserProfile user = new UserProfile();
        user.setUserCredentials(credentials);
        user.setFirstName("Carmen");
        user.setLastName("San Diego");
        user.setBirthdate(Date.from(Instant.parse("2004-12-03T10:15:30.00Z")));
        user.setZip("84095");
        user.setSex('F');
        user.setLastLogin(Date.from(Instant.now()));

        return user;
    }

}
