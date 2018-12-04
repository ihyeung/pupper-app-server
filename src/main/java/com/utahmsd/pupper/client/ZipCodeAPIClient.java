package com.utahmsd.pupper.client;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.util.ZipcodeResult;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;

import static com.utahmsd.pupper.util.ValidationUtils.isValidZipcode;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Named
@Singleton
public class ZipCodeAPIClient {
    private static final String apiKey = System.getProperty("zipCode.api.key", "yB2CXPwRWaX4K1ZqVAHYAPaT6FjSgYVZoJYAw2mQIexjio7L2xtWLboez3XgBp7Y");
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeAPIClient.class);

    private static final Header[] HEADERS = {
            new BasicHeader("Content-Type", String.valueOf(APPLICATION_JSON)),
            new BasicHeader("Accept", String.valueOf(APPLICATION_JSON))};

    private static final String BASE_URL = "https://www.zipcodeapi.com/rest/%s/%s.json/%s/%s/miles";
    private static final String DIST_BETWEEN_ZIPCODES = String.format(BASE_URL, apiKey, "distance", "%s", "%s");
    private static final String DIST_BETWEEN_ZIPCODE_LIST = String.format(BASE_URL, apiKey, "multi-distance", "%s", "%s");
    private static final String ZIP_CODES_RADIUS = String.format(BASE_URL, apiKey, "radius", "%s", "%d");
    public static final int MAX_RADIUS = 50;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ZipCodeAPIClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Map<String,Integer> getDistanceBetweenMultipleZipcodes(String zipcode, List<String> zipcodeList) {
        Map<String,Integer> distanceMap = new HashMap<>();
        String zipString = createZipcodeString(zipcodeList);
        if (!isValidZipcode(zipcode) || zipString == null) {
            return distanceMap;
        }
        String responseBody = executeHttpGet(DIST_BETWEEN_ZIPCODE_LIST, zipcode, zipString);
        Map<String,Double> distances = (Map<String, Double>) new JacksonJsonParser().parseMap(responseBody).get("distances");
        distances.forEach((k,v) -> {
            LOGGER.info("Key: '{}', Value: '{}'", k, v);
            distanceMap.put(k, v.intValue());
        });
        return distanceMap;
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
            if (response.getStatusLine().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                LOGGER.error("Maximum number of requests to ZipCode API exceeded. Please try again in an hour.");
                return -1;
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            String distance = new JacksonJsonParser().parseMap(responseBody).get("distance").toString();
            return Math.round(Float.parseFloat(distance));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getZipCodesInRadius(String zipcode, String radius) {
        List<String> zipCodes = new ArrayList<>();
        if (!isValidRadius(radius) || !isValidZipcode(zipcode) || Integer.valueOf(radius) == 0) {
            return zipCodes;
        }
        HttpGet httpGet = new HttpGet(String.format(ZIP_CODES_RADIUS, zipcode, radius));
        httpGet.setHeaders(HEADERS);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                LOGGER.error("Maximum number of requests to ZipCode API exceeded. Please try again in an hour.");
                return zipCodes;
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            List<ZipcodeResult> zipcodeResults = parseZipcodeResultsFromApiResponse(responseBody);
            zipcodeResults.forEach(each -> zipCodes.add(each.getZipcode()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipCodes;
    }

    private String executeHttpGet(String url, String zipcode, String apiParam) {
        HttpGet httpGet = new HttpGet(String.format(url, zipcode, apiParam));
        LOGGER.info("Sending HTTP GET to '{}'", httpGet.getURI());
        httpGet.setHeaders(HEADERS);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                LOGGER.error("Maximum number of requests to ZipCode API exceeded. Please try again in an hour.");
                return null;
            }
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ZipcodeResult> parseZipcodeResultsFromApiResponse(String response) {
        if (!StringUtils.isNullOrEmpty(response)) {
            Map<String, Object> responseMap = new BasicJsonParser().parseMap(response);
            Object zipCodeData = responseMap.getOrDefault("zip_codes", null);

            return objectMapper.convertValue(zipCodeData, new TypeReference<List<ZipcodeResult>>() {});
        }
        return Collections.emptyList();
    }

    private boolean isValidRadius(String input) {
        return Integer.valueOf(input) >= 0 && Integer.valueOf(input) <= MAX_RADIUS;
    }

    private String createZipcodeString(List<String> list) {
        StringBuilder zipString = new StringBuilder();
        for (String zip : list) {
            if (!isValidZipcode(zip)) {
                return null;
            }
            zipString.append(",").append(zip);
        }
        return zipString.substring(1);
    }

}
