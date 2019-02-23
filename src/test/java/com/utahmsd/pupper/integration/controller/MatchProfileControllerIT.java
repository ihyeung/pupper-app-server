package com.utahmsd.pupper.integration.controller;

import com.utahmsd.pupper.PupperApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("TEST")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PupperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchProfileControllerIT {
}
