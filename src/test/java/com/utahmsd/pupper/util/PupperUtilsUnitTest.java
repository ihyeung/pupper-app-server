package com.utahmsd.pupper.util;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static com.utahmsd.pupper.util.PupperUtils.calculateAge;
import static org.junit.Assert.assertEquals;

public class PupperUtilsUnitTest {

    private static final long SECONDS_DAY = 86_400L;
    private static final long SECONDS_WEEK = SECONDS_DAY * 7;

    @Test
    public void testCalculateAge_birthdateIsToday() {
        assertEquals(calculateAge(Date.from(Instant.now())), "0Y0M0W0D");
    }

    @Test
    public void testCalculateAge_invalidFutureBirthdate() {
        assertEquals(calculateAge(Date.from(Instant.now().plusSeconds(SECONDS_DAY*7))), "0Y0M0W0D");
    }

    @Test
    public void testCalculateAge_validBirthdate() {

        Instant oneWeekOld = Instant.now().minusSeconds(SECONDS_WEEK);
        Instant tenDaysOld = Instant.now().minusSeconds(SECONDS_WEEK + (3 * SECONDS_DAY));

        assertEquals(calculateAge(Date.from(oneWeekOld)), "0Y0M1W0D");
        assertEquals(calculateAge(Date.from(tenDaysOld)), "0Y0M1W3D");

        assertEquals(calculateAge(Date.from(Instant.now().minusSeconds(SECONDS_DAY * 366))), "1Y0M0W1D");
    }
}