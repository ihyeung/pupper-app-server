package com.utahmsd.pupper.acceptance.controller;

import com.utahmsd.pupper.TestUtils;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

abstract class BaseAcceptanceTest {

    static String authenticateAndRetrieveAuthToken() {
        return
                    given().
                            relaxedHTTPSValidation().
                            log().all().
                            contentType(ContentType.JSON).
                            body(TestUtils.createUserAccount()).
                    when().
                            post("/login").
                    then().
                            log().all().
                            statusCode(200).
                            extract().response().header("Authorization");
        }

}
