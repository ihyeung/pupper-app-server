package com.utahmsd.pupper.client;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utahmsd.pupper.exception.ApiException;
import com.utahmsd.pupper.util.ZipCodeRadiusResult;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.utahmsd.pupper.util.ValidationUtils.isValidZipCodeRadius;
import static com.utahmsd.pupper.util.ValidationUtils.isValidZipcode;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Component
public class ZipCodeAPIClient {
    private static final String apiKey = System.getProperty("zipCode.api.key", "");
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeAPIClient.class);

    private static final Header[] HEADERS = {
            new BasicHeader("Content-Type", String.valueOf(APPLICATION_JSON)),
            new BasicHeader("Accept", String.valueOf(APPLICATION_JSON))};

    private static final String BASE_URL = "https://www.zipcodeapi.com/rest/%s/%s.json/%s/%s/mile";
    private static final String DIST_BETWEEN_ZIPCODES = String.format(BASE_URL, apiKey, "distance", "%s", "%s");
    private static final String DIST_BETWEEN_ZIPCODE_LIST = String.format(BASE_URL, apiKey, "multi-distance", "%s", "%s");
    private static final String ZIP_CODES_RADIUS = String.format(BASE_URL, apiKey, "radius", "%s", "%s");
    public static final int MAX_RADIUS = 50;
    public static final int MIN_RADIUS = 3;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final JacksonJsonParser jacksonJsonParser;

    @Autowired
    public ZipCodeAPIClient(HttpClient httpClient, ObjectMapper objectMapper, JacksonJsonParser jacksonJsonParser) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.jacksonJsonParser = jacksonJsonParser;
    }

    public Map<String,Integer> getDistanceBetweenZipCodeAndMultipleZipCodes(String zipcode,
                                                                            List<String> zipcodeList) throws IOException {
        Map<String,Integer> distanceMap = new HashMap<>();
        String zipString = createMultiZipcodeStringForUrl(zipcode, zipcodeList);
        if (!isValidZipcode(zipcode) || zipString == null) {
            return distanceMap;
        }
        if (zipcodeList.contains(zipcode)) {
            distanceMap.put(zipcode, 0);
        }
        String responseBody = executeHttpGet(DIST_BETWEEN_ZIPCODE_LIST, zipcode, zipString);
        if (!StringUtils.isNullOrEmpty(responseBody)) {
            Map<String, Object> distances = jacksonJsonParser.parseMap(responseBody);
            String[] distanceList = manuallyParseZipcodeDIstanceResponseObj(distances);
            for (String each: distanceList) {
                String[] zipDistKeyValPair = each.split("=");
                Integer roundedDistance = Math.round(Float.parseFloat(zipDistKeyValPair[1]));
                LOGGER.info("Adding the following zipcode and its calculated distance: <{},{}>",
                        zipDistKeyValPair[0], roundedDistance);
                distanceMap.put(zipDistKeyValPair[0], roundedDistance);
            }
        }
        return distanceMap;
    }

    private String[] manuallyParseZipcodeDIstanceResponseObj(Map<String, Object> distances) {
        Object zipCodeData = distances.getOrDefault("distances", null);
        String zipcodeString =
                zipCodeData.toString().replace("{", "").replace("}","");
        return zipcodeString.split(", ");
    }

    public Integer getDistanceBetweenZipcodes(String zipcode, String zipcode1) throws IOException {
        if (!isValidZipcode(zipcode) || !isValidZipcode(zipcode1) || zipcode.equals(zipcode1)) {
            return 0;
        }
        String responseBody = executeHttpGet(DIST_BETWEEN_ZIPCODES, zipcode, zipcode1);
        if (!StringUtils.isNullOrEmpty(responseBody)) {
            String distance = jacksonJsonParser.parseMap(responseBody).get("distance").toString();
            return Math.round(Float.valueOf(distance));
        }
        return 0;
    }

    public List<String> getZipCodesInRadius(String zipcode, String radius) throws IOException {
        List<String> zipCodes = new ArrayList<>();
        if (!isValidZipCodeRadius(radius) || !isValidZipcode(zipcode) || Integer.valueOf(radius) == 0) {
            LOGGER.error("Invalid zipcode radius '{}' or zipcode: '{}'", radius, zipcode);
            return zipCodes;
        }
        String responseBody;
            responseBody = executeHttpGet(ZIP_CODES_RADIUS, zipcode, radius);
        if (!StringUtils.isNullOrEmpty(responseBody)) {
            List<ZipCodeRadiusResult> zipcodeResults = parseZipCodeResultsFromApiResponse(responseBody);
            zipcodeResults.forEach(each -> zipCodes.add(each.getZipcode()));
        }
        return zipCodes;
    }

    private String executeHttpGet(String url, String zipcode, String apiParam) throws IOException {
        HttpGet httpGet = new HttpGet(String.format(url, zipcode, apiParam));
        LOGGER.info("Sending HTTP GET to '{}'", httpGet.getURI());
        httpGet.setHeaders(HEADERS);

        HttpResponse response;
        response = httpClient.execute(httpGet);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            String API_USAGE_ERROR = "Maximum number of requests to ZipCode API exceeded. Please try again in an hour.";
            LOGGER.error(API_USAGE_ERROR);
            throw new ApiException(API_USAGE_ERROR);
        }
        else if (response.getStatusLine().getStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
            String API_KEY_ERROR = "API key is invalid";
            LOGGER.error(API_KEY_ERROR);
            throw new ApiException(API_KEY_ERROR);
        }
        return EntityUtils.toString(response.getEntity());
    }

    private List<ZipCodeRadiusResult> parseZipCodeResultsFromApiResponse(String response) {
        if (!StringUtils.isNullOrEmpty(response)) {
            Map<String, Object> responseMap = jacksonJsonParser.parseMap(response);
            Object zipCodeData = responseMap.getOrDefault("zip_codes", null);

            return objectMapper.convertValue(zipCodeData, new TypeReference<List<ZipCodeRadiusResult>>() {});
        }
        return Collections.emptyList();
    }

    private String createMultiZipcodeStringForUrl(String zipcode, List<String> list) {
        if (list.isEmpty()) {
            return null;
        }
        StringBuilder zipString = new StringBuilder();
        for (String zip : list) {
            if (!isValidZipcode(zip)) {
                return null;
            }
            if (!zip.equals(zipcode)) { //Skip concatenating a zip code if it matches the zipcode making the api call
                zipString.append(",").append(zip);
            }
        }
        return zipString.length() > 1 ? zipString.substring(1) : null;
    }

}
