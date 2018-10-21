package com.utahmsd.pupper.util;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static com.utahmsd.pupper.util.Utils.getIsoFormatDate;
import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {

    @Test
    public void testComputeHash() {
    }

    @Test
    public void testConcatBytes() {
    }

    @Test
    public void testGenerateSalt() {
    }

    @Test
    public void testGetIsoFormatDate() {
        Date date = Date.from(Instant.parse("2014-12-03T10:15:30.00Z"));
        assertEquals(getIsoFormatDate(date), "2014-12-03T10:15:30.000 UTC");
    }
}