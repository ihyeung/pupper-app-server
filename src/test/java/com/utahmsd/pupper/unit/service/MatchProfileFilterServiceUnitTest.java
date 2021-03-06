package com.utahmsd.pupper.unit.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.utahmsd.pupper.service.filter.MatchProfileFilterService.isValidMatchProfileSort;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MatchProfileFilterServiceUnitTest {

    @Test
    public void testIsValidMatchProfileSort() {
        assertTrue(isValidMatchProfileSort("score"));
        assertTrue(isValidMatchProfileSort("birthdate"));
        assertTrue(isValidMatchProfileSort("breed"));
        assertTrue(isValidMatchProfileSort("energyLevel"));
        assertTrue(isValidMatchProfileSort("lifeStage"));
        assertTrue(isValidMatchProfileSort("aboutMe"));
        assertTrue(isValidMatchProfileSort("profileImage"));
        assertTrue(isValidMatchProfileSort("id"));
        assertTrue(isValidMatchProfileSort("names"));
        assertTrue(isValidMatchProfileSort("zipRadius"));
        assertTrue(isValidMatchProfileSort("isDefault"));
        assertTrue(isValidMatchProfileSort("isHidden"));
        assertTrue(isValidMatchProfileSort("showSimilar"));

        assertFalse(isValidMatchProfileSort(null));
        assertFalse(isValidMatchProfileSort(""));
        assertFalse(isValidMatchProfileSort("num_dogs"));
        assertFalse(isValidMatchProfileSort("profile_image"));
        assertFalse(isValidMatchProfileSort("life_stage"));
        assertFalse(isValidMatchProfileSort("zip_radius"));
    }
}