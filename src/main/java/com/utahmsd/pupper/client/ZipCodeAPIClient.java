package com.utahmsd.pupper.client;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.util.ZipcodeResult;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JacksonJsonParser;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.utahmsd.pupper.util.ValidationUtils.isValidZipcode;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Named
@Singleton
public class ZipCodeAPIClient {
    private static final String apiKey = System.getProperty("zipCode.api.key", "");

    private static final Header[] HEADERS = {
            new BasicHeader("Content-Type", String.valueOf(APPLICATION_JSON)),
            new BasicHeader("Accept", String.valueOf(APPLICATION_JSON))};

    private static final String BASE_URL = "https://www.zipcodeapi.com/rest/%s/%s.json/%s/%s/miles";
    private static final String DIST_BETWEEN_ZIPCODES = String.format(BASE_URL, apiKey, "distance", "%s", "%s");
    private static final String ZIP_CODES_RADIUS = String.format(BASE_URL, apiKey, "radius", "%s", "%d");
    private static final int MAX_RADIUS = 500;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ZipCodeAPIClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Integer getDistanceBetweenZipcodes(String zipcode, String zipcode1) {
        if (!isValidZipcode(zipcode) || !isValidZipcode(zipcode1) || zipcode.equals(zipcode1)) {
            return 0;
        }
        HttpGet httpGet = new HttpGet(String.format(DIST_BETWEEN_ZIPCODES, zipcode, zipcode1));
        httpGet.setHeaders(HEADERS);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            String distance = new JacksonJsonParser().parseMap(responseBody).get("distance").toString();
            return Math.round(Float.parseFloat(distance));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getZipCodesInRadius(String zipcode, int radius) {
        List<String> zipCodes = new ArrayList<>();
        if (!isValidRadius(radius) || !isValidZipcode(zipcode) || radius == 0) {
            return zipCodes;
        }
        HttpGet httpGet = new HttpGet(String.format(ZIP_CODES_RADIUS, zipcode, radius));
        httpGet.setHeaders(HEADERS);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            List<ZipcodeResult> zipcodeResults = parseZipcodeResultsFromApiResponse(responseBody);
            zipcodeResults.forEach(each -> zipCodes.add(each.getZipcode()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipCodes;
    }

    private List<ZipcodeResult> parseZipcodeResultsFromApiResponse(String response) {
        if (!StringUtils.isNullOrEmpty(response)) {
            Map<String, Object> responseMap = new BasicJsonParser().parseMap(response);
            Object zipCodeData = responseMap.getOrDefault("zip_codes", null);

            return objectMapper.convertValue(zipCodeData, new TypeReference<List<ZipcodeResult>>() {});
        }
        return Collections.emptyList();
    }

    private boolean isValidRadius(int input) {
        return input >= 0 && input <= MAX_RADIUS;
    }
}
