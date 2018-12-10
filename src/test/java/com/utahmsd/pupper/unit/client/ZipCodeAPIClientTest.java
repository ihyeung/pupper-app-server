package com.utahmsd.pupper.unit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.client.ZipCodeAPIClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(MockitoJUnitRunner.class)
public class ZipCodeAPIClientTest {

//    private ZipCodeAPIClient zipCodeAPIClient;
//
//    @Mock
//    private HttpClient httpClient;
//    @Mock
//    private ObjectMapper objectMapper;
////
//    private static final String DEFAULT_ZIP = "84095";
//    private List<ZipcodeResult> mockResults;
//    private HttpResponse response;
//
//    @Before
//    public void init() {
//        zipCodeAPIClient = new ZipCodeAPIClient(httpClient, objectMapper, apiKey);
//
//        mockResults = new ArrayList<>();
//        mockResults.add(new ZipcodeResult("84009", 1.744, "South Jordan", "UT"));
//        mockResults.add(new ZipcodeResult("84095", 0, "South Jordan", "UT"));
//        mockResults.add(new ZipcodeResult("84088", 2.773, "West Jordan", "UT"));
//
//    }

    private static final String DEFAULT_ZIP = "84095";
    private HttpClient httpClient = HttpClients.createDefault();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ZipCodeAPIClient zipCodeAPIClient;

    @Before
    public void init() {
        zipCodeAPIClient = new ZipCodeAPIClient(httpClient, objectMapper);
    }

    @Test
    public void testGetZipCodesInRadius() throws IOException {
//        when(httpClient.execute(any())).thenReturn(response);

        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "3").size()).isEqualTo(3);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "1").size()).isEqualTo(1);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "0").size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius(DEFAULT_ZIP, "-10").size()).isEqualTo(0);
        assertThat(zipCodeAPIClient.getZipCodesInRadius("123456789", "5").size()).isEqualTo(0);
    }

    @Test
    public void testGetDistanceBetweenZipcodes() {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, "84601")).isNotZero();
    }

    @Test
    public void testGetDistanceBetweenZipcodes_invalidZipcode() {
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, DEFAULT_ZIP)).isEqualTo(0);
        assertThat(zipCodeAPIClient.getDistanceBetweenZipcodes(DEFAULT_ZIP, "ello")).isEqualTo(0);
    }

    @Test
    public void testGetDistanceBetweenMultipleZipcodes() {
        List<String> zipcodeList = Arrays.asList("84601", "84088");
        assertThat(zipCodeAPIClient.getDistanceBetweenMultipleZipcodes(DEFAULT_ZIP, zipcodeList)).containsKeys("84601", "84088");
    }

    @Test
    public void testGetDistanceBetweenMultipleZipcodes_listContainsInvalidZipcode() {
        List<String> zipcodeList = Arrays.asList("84601", "84088", "hello");
        assertThat(zipCodeAPIClient.getDistanceBetweenMultipleZipcodes(DEFAULT_ZIP, zipcodeList)).isEmpty();
    }
}