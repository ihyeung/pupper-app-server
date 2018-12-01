package com.utahmsd.pupper.service.filter;

import org.junit.Test;

import static com.utahmsd.pupper.service.filter.MatchProfileFilterService.isValidMatchProfileSort;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchProfileFilterServiceUnitTest {

    @Test
    public void testIsValidMatchProfileSort() {
        assertTrue(isValidMatchProfileSort("score"));
        assertTrue(isValidMatchProfileSort("birthdate"));
        assertTrue(isValidMatchProfileSort("breed"));
        assertTrue(isValidMatchProfileSort("energy"));
        assertTrue(isValidMatchProfileSort("id"));
        assertTrue(isValidMatchProfileSort("names"));

        assertFalse(isValidMatchProfileSort(null));
        assertFalse(isValidMatchProfileSort(""));
        assertFalse(isValidMatchProfileSort("num_dogs"));

    }
}