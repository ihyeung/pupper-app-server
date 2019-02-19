package com.utahmsd.pupper.acceptance.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.client.ZipCodeAPIClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.json.JacksonJsonParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipCodeAPIClientAT {

    private ZipCodeAPIClient zipCodeAPIClient;

    private static final String DEFAULT_ZIP = "84095";

    @Before
    public void init() {
        zipCodeAPIClient =
                new ZipCodeAPIClient(HttpClients.createDefault(), new ObjectMapper(), new JacksonJsonParser());
    }

    @Test
    public void testGetZipCodesInRadius() throws IOException {

        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "3").size()).isEqualTo(3);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "1").size()).isEqualTo(1);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "0").size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "-10").size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius("123456789", "5").size()).isEqualTo(0);
    }

    @Test
    public void testGetDistanceBetweenZipcodes() throws IOException {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, "84601")).isNotZero();
    }


    @Test
    public void testGetDistanceBetweenZipcodes_distanceBetweenDuplicateZipCodes() throws IOException {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, DEFAULT_ZIP)).isEqualTo(0);
    }

    @Test
    public void testGetDistanceBetweenZipcodes_invalidZipcode() throws IOException {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, "ello")).isEqualTo(0);
    }

    @Test
    public void testGetDistanceBetweenMultipleZipcodes() throws IOException {
        List<String> zipcodeList = Arrays.asList(DEFAULT_ZIP, "84088");
        Map<String, Integer> distances =
                zipCodeAPIClient.getDistanceBetweenZipCodeAndMultipleZipCodes(DEFAULT_ZIP, zipcodeList);
        assertThat(distances).containsKey( "84088");
        assertThat(distances.get("84088")).isNotZero();

    }

    @Test
    public void testGetDistanceBetweenMultipleZipcodes_listContainsInvalidZipcode() throws IOException {
        List<String> zipcodeList = Arrays.asList("84601", "84088", "hello");
        assertThat(zipCodeAPIClient.getDistanceBetweenZipCodeAndMultipleZipCodes(DEFAULT_ZIP, zipcodeList)).isEmpty();
    }
}