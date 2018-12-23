package com.utahmsd.pupper.util;

import org.junit.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

import static com.utahmsd.pupper.util.ValidationUtils.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTest {

    @Test
    public void testIsValidZipcode() {
        assertTrue(isValidZipcode("84095"));
        assertFalse(isValidZipcode("123456"));
        assertFalse(isValidZipcode("hello"));
        assertFalse(isValidZipcode("12345-6578")); //API doesn't support zip+4 zipcodes
        assertFalse(isValidZipcode("$%@^1"));
    }

    @Test
    public void testIsValidZipCodeRadius() {
        assertTrue(isValidZipCodeRadius("0"));
        assertTrue(isValidZipCodeRadius("50"));
        assertFalse(isValidZipCodeRadius("123456"));
        assertFalse(isValidZipCodeRadius("-10"));
        assertFalse(isValidZipCodeRadius("TEST"));
    }

    @Test
    public void testIsValidEmail_validEmailInput() {
        assertTrue(isValidEmail("test123@test.com"));
        assertTrue(isValidEmail("test_123@test.com"));
        assertTrue(isValidEmail("test-123@test.com"));
        assertTrue(isValidEmail("test.test@test.com"));
        assertTrue(isValidEmail("test@hotmail.co.uk"));
        assertTrue(isValidEmail("123456789@test.com"));
        assertTrue(isValidEmail("123456789@utah.edu"));
    }

    @Test
    public void testIsValidEmail_invalidEmailInput() {
        assertFalse(isValidEmail("email"));
        assertFalse(isValidEmail("1234"));
        assertFalse(isValidEmail("emailwith @ spaces.com"));
        assertFalse(isValidEmail("helloemail@test"));
        assertFalse(isValidEmail("hello&#*email@test.com"));
        assertFalse(isValidEmail("hello/;email@test.com"));
        assertFalse(isValidEmail("   123456789@test.com "));
        assertFalse(isValidEmail("hello.email@test..com"));
    }

    @Test
    public void testIsValidDate_validDate() {
        assertTrue(isValidDate(new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()))));
        assertTrue(isValidDate("2018-11-29"));
        assertTrue(isValidDate("2018-01-31"));
        assertTrue(isValidDate("2000-12-31"));
    }

    @Test
    public void testIsValidDate_invalidDateFormat() {
        assertFalse(isValidDate("hello"));
        assertFalse(isValidDate("12-04-90"));
        assertFalse(isValidDate("12-04-1990"));
        assertFalse(isValidEmail("12/4/1990"));
        assertFalse(isValidDate("2018-1-9"));
        assertFalse(isValidDate("1800-21-09"));
        assertFalse(isValidDate("2030-21-09"));
    }

    @Test
    public void testIsValidNumericInput() {
        assertTrue(isValidNumericInput("5"));
        assertTrue(isValidNumericInput("123456789"));
        assertFalse(isValidNumericInput(""));
        assertFalse(isValidNumericInput("TEST"));
        assertFalse(isValidNumericInput("123TEST123"));

    }
}