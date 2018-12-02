package com.utahmsd.pupper.acceptance;

import com.utahmsd.pupper.dao.entity.UserAccount;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

abstract class BaseAcceptanceTest {

    static UserAccount createUserAccountForTests() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("createUserProfileTest@test.com");
        userAccount.setPassword("TESTPASSWORD");
        userAccount.setId(3L);

        return userAccount;
    }

    static String authenticateAndRetrieveAuthToken() {
        return
                    given().
                            relaxedHTTPSValidation().
                            log().all().
                            contentType(ContentType.JSON).
                            body(createUserAccountForTests()).
                    when().
                            post("/login").
                    then().
                            log().all().
                            statusCode(200).
                            extract().response().header("Authorization");
        }

}
