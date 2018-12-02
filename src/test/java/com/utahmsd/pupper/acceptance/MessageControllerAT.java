package com.utahmsd.pupper.acceptance;

import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperMessage;
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

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class MessageControllerAT extends BaseAcceptanceTest {

    private static final String TEST_URL = System.getProperty("TEST_URL", "http://localhost:5000");

    private static String accessToken;

    @Inject
    MatchProfileRepo matchProfileRepo;

    @Before
    public void init(){
        RestAssured.baseURI = TEST_URL;
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;

        accessToken = authenticateAndRetrieveAuthToken();
    }

    @Test
    public void testGetMessagesWithLimit() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
                param("limit", 3).
        when().
                get("/message").
        then().
                log().all().
                statusCode(200).
                body(notNullValue());
    }

    @Test
    public void sendMessageToMatch() {
        given().
                relaxedHTTPSValidation().
                log().all().
                contentType(ContentType.JSON).
                header(new Header("Authorization", accessToken)).
                param("sendFrom", 1).
                param("sendTo", 5).
                body(createMessage(1L, 5L, "MESSAGE FROM ACCEPTANCE TESTS from id=1 to id=5")).
        when().
                post("/message").
        then().
                log().all().
                statusCode(200).
                body(notNullValue());
    }

    PupperMessage createMessage(Long matchId1, Long matchId2, String message) {
        Optional<MatchProfile> matchProfile1 = matchProfileRepo.findById(matchId1);
        Optional<MatchProfile> matchProfile2 = matchProfileRepo.findById(matchId2);
        PupperMessage pupperMessage = new PupperMessage();
        pupperMessage.setMatchProfileSender(matchProfile1.get());
        pupperMessage.setMatchProfileReceiver(matchProfile2.get());
        pupperMessage.setMessage(message);
        pupperMessage.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(Date.from(Instant.now())));

        System.out.println(pupperMessage.getTimestamp());

        return pupperMessage;
    }
}