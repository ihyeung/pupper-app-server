package com.utahmsd.pupper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ZipCodeAPIClientTest {

    private static final String DEFAULT_ZIP = "84095";

    private HttpClient httpClient = HttpClients.createDefault();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ZipCodeAPIClient zipCodeAPIClient = new ZipCodeAPIClient(httpClient, objectMapper);

    @Test
    public void testGetZipCodesInRadius() {
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, 5).size()).isEqualTo(7);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, 1).size()).isEqualTo(1);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, 0).size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, -10).size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius("123456789", 5).size()).isEqualTo(0);

    }

    @Test
    public void testGetDistanceBetweenZipcodes() {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, DEFAULT_ZIP)).isEqualTo(0);
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, "84601")).isEqualTo(27);

    }


}