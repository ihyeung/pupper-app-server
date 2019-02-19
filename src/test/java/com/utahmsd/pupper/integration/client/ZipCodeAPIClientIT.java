package com.utahmsd.pupper.integration.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.client.ZipCodeAPIClient;
import com.utahmsd.pupper.util.ZipCodeRadiusResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.json.JacksonJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ZipCodeAPIClientIT {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JacksonJsonParser jacksonJsonParser;

    @Mock
    private HttpEntity httpEntity;

    @InjectMocks
    private ZipCodeAPIClient zipCodeAPIClient;

    private HttpResponse response;
    List<ZipCodeRadiusResult> mockZipCodeResults = new ArrayList<>();


    @Before
    public void init() throws IOException {
        mockZipCodeResults.add(new ZipCodeRadiusResult("84088", 2.773, "West Jordan", "UT"));
        mockZipCodeResults.add(new ZipCodeRadiusResult("84009", 1.744, "South Jordan", "UT"));
        mockZipCodeResults.add(new ZipCodeRadiusResult("84095", 0.0, "South Jordan", "UT"));

        BasicStatusLine statusLine =
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "");

        response = Mockito.mock(HttpResponse.class);
        given(response.getStatusLine()).willReturn(statusLine);
        given(mockHttpClient.execute(any())).willReturn(response);
        given(response.getEntity()).willReturn(httpEntity);

        given(httpEntity.getContent()).willReturn(Mockito.mock(InputStream.class));
    }

    @Ignore
    @Test
    public void testGetZipCodesInRadius_success() throws IOException {
        HashMap<String, Object> zipCodeMap = new HashMap<>();
        zipCodeMap.put("zip_codes", Mockito.mock(Object.class));

        given(jacksonJsonParser.parseMap(anyString())).willReturn(zipCodeMap);


        given(objectMapper.convertValue(any(Object.class),
                ArgumentMatchers.<com.fasterxml.jackson.core.type.TypeReference<?>>any())).willReturn(mockZipCodeResults);

        zipCodeAPIClient.getZipCodesInRadius("84095", "3");

        verify(objectMapper).convertValue(any(Object.class),
                ArgumentMatchers.<com.fasterxml.jackson.core.type.TypeReference<?>>any());
        verify(mockHttpClient).execute(any());
    }

    @Test
    public void testGetZipCodesInRadius_successfulHttpClientCall_nullResponse() throws IOException {
        zipCodeAPIClient.getZipCodesInRadius("84095", "3");

        verifyZeroInteractions(objectMapper);
        verify(mockHttpClient).execute(any());
    }

    @Test
    public void testGetZipCodesInRadius_invalidZipCode() throws IOException {
        zipCodeAPIClient.getZipCodesInRadius("TEST", "3");

        verifyZeroInteractions(mockHttpClient);
        verifyZeroInteractions(objectMapper);

    }

    @Test
    public void testGetZipCodesInRadius_invalidRadius() throws IOException {
        zipCodeAPIClient.getZipCodesInRadius("84095", "hello");

        verifyZeroInteractions(mockHttpClient);
        verifyZeroInteractions(objectMapper);

    }

    @Test
    public void testGetDistanceBetweenZipCodes() {

    }

    @Test
    public void testGetDistanceBetweenZipCodesInList() {


    }


}
