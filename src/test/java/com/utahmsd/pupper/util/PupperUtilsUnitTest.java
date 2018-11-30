package com.utahmsd.pupper.util;

import org.junit.Test;

import java.time.Instant;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import static com.utahmsd.pupper.util.PupperUtils.DEFAULT_AGE;
import static com.utahmsd.pupper.util.PupperUtils.createAgeStringFromDate;
import static org.junit.Assert.assertEquals;

public class PupperUtilsUnitTest {

    private static final long SECONDS_DAY = 86_400L;
    private static final long SECONDS_WEEK = SECONDS_DAY * 7;

    @Test
    public void testCreateAgeStringFromDate_invalidDate() {
        assertEquals(DEFAULT_AGE, createAgeStringFromDate(Date.from(Instant.now())));
        assertEquals(DEFAULT_AGE, createAgeStringFromDate(Date.from(Instant.now().plusSeconds(SECONDS_WEEK*7))));
        assertEquals(DEFAULT_AGE, createAgeStringFromDate(null));
    }

    @Test
    public void testCreateAgeStringFromDate_validBirthdate() {
        Instant oneWeekOld = Instant.now().minusSeconds(SECONDS_WEEK);
        Instant tenDaysOld = Instant.now().minusSeconds(SECONDS_WEEK + (3 * SECONDS_DAY));
        Date twentyNineDays = Date.from(Instant.now().minus(29, ChronoUnit.DAYS));
        Date fourMonths = Date.from(Instant.now().minus(31*4, ChronoUnit.DAYS));
        Date sixMonthsOld = Date.from(Instant.now().minus(185, ChronoUnit.DAYS));
        Date twoYears = Date.from(Instant.now().minus(750, ChronoUnit.DAYS));

        assertEquals("1 days old", createAgeStringFromDate(Date.from(Instant.now().minusSeconds(SECONDS_DAY))));
        assertEquals("1 weeks old", createAgeStringFromDate(Date.from(oneWeekOld)));
        assertEquals("1 weeks old", createAgeStringFromDate(Date.from(tenDaysOld)));
        assertEquals("4 weeks old", createAgeStringFromDate(twentyNineDays));
        assertEquals("4 months old", createAgeStringFromDate(fourMonths));
        assertEquals("6 months old", createAgeStringFromDate(sixMonthsOld));
        assertEquals("2 years old", createAgeStringFromDate(twoYears));
    }
}