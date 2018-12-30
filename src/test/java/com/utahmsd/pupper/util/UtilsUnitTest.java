package com.utahmsd.pupper.util;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static com.utahmsd.pupper.util.Utils.getIsoFormatTimestamp;
import static com.utahmsd.pupper.util.Utils.getIsoFormatTimestampFromDate;
import static org.junit.Assert.assertEquals;

public class UtilsUnitTest {

    @Test
    public void testGetIsoFormatDate() {
        Date date = Date.from(Instant.parse("2014-12-03T10:15:30.00Z"));
        assertEquals(getIsoFormatTimestampFromDate(date, null), "2014-12-03T10:15:30Z");
        assertEquals(getIsoFormatTimestampFromDate(date, "UTC"), "2014-12-03T10:15:30Z");

        Instant now = Instant.now();
        assertEquals(getIsoFormatTimestamp(now), getIsoFormatTimestampFromDate(Date.from(now), null));
    }
}