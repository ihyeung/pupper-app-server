package com.utahmsd.pupper.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.StringUtils;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import com.utahmsd.pupper.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static com.amazonaws.regions.Regions.US_EAST_1;
import static com.utahmsd.pupper.dto.ImageUploadResponse.createImageUploadResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_PATH_VARIABLE;
import static org.springframework.http.HttpStatus.*;

@Named
@Singleton
public class AmazonAwsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonAwsClient.class);
    private final String MATCH_PROFILE_IMAGE_UPDATE_ENDPOINT = "/user/%d/matchProfile/%d?profilePic=%s";
    private final String USER_PROFILE_IMAGE_UPDATE_ENDPOINT = "/user/%d?profilePic=%s";

    private final String MATCH_PROFILE_GET_ENDPOINT = "/user/%d/matchProfile/%d";
    private final String USER_PROFILE_GET_ENDPOINT = "/user/%d";

    public static final long MAX_IMAGE_BYTES = 1_048_576L;

    @Value("${spring.jersey.application-path:}")
    private String baseUrl;

    private final HttpClient httpClient;
    private final AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String s3endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Autowired
    public AmazonAwsClient(AmazonS3 s3client, HttpClient httpClient) {
        this.s3client = s3client;
        this.httpClient = httpClient;
    }

    private ImageUploadResponse uploadFileToS3(MultipartFile file, String fileName) {
        String outputFilePath = String.format("%s/%s/%s", s3endpointUrl, bucketName, fileName);
        String imageUrl = outputFilePath.replace(US_EAST_1 + ".", "");

        File inputFile = null;
        try {
            inputFile = convertMultiPartToFile(file);
        } catch (IOException e) {
            LOGGER.error("IO EXCEPTION: {}", e);
            e.printStackTrace();
        }
        try {
            s3client.putObject(new PutObjectRequest(bucketName, fileName, inputFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            LOGGER.error("File upload to S3 bucket failed: {}", e.getMessage());
            e.printStackTrace();
            return createImageUploadResponse(false, "", INTERNAL_SERVER_ERROR,
                    String.format("File upload to S3 bucket failed: '%s'", e.getMessage()));
        }
        return  createImageUploadResponse(true, imageUrl, OK, DEFAULT_DESCRIPTION);
    }

    public ImageUploadResponse uploadFileByUserAndMatchProfile(MultipartFile multipartFile, Long userId, Long matchProfileId, String authToken) {
        ImageUploadResponse invalidSizeResponse = processFileUploadsExceedingLimit(multipartFile);
        if (invalidSizeResponse != null) {
            return invalidSizeResponse;
        }
        //Use http client to hit GET endpoint in matchProfileController to verify matchProfile exists
        Object getResponseObject= doGetRequest(userId, matchProfileId, authToken, "matchProfiles");
        MatchProfile matchProfile = null;
        try {
            matchProfile = MatchProfile.createFromObject(getResponseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (matchProfile == null || !matchProfile.getUserProfile().getId().equals(userId)) {
            LOGGER.error("Either matchProfileId={} was not found, or userProfileId contained in the matchProfile result " +
                    "corresponding to matchProfileId={} does not match the path userProfileId={}",
                    matchProfileId, matchProfileId, userId);
            return createImageUploadResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        String fileName = generateFileNameByUserAndMatchProfileId(userId, matchProfileId);
        ImageUploadResponse response = uploadFileToS3(multipartFile, fileName);
        if (response.getResponseCode() == 200) {
            //Use http client to hit updateProfileImageForMatchProfile endpoint in matchProfileController
            HttpStatus postResponseStatus = doPostRequest(userId, matchProfileId, authToken, response.getImageUrl());
            if (postResponseStatus.is2xxSuccessful()) {
                return createImageUploadResponse(true, response.getImageUrl(), HttpStatus.OK, DEFAULT_DESCRIPTION);
            }
            return createImageUploadResponse(false, response.getImageUrl(), postResponseStatus,
                    String.format("Error response %d returned when updating profilePic for userId=%d and matchProfile=%d",
                            postResponseStatus.value(), userId, matchProfileId));
        }

        return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                String.format("Failure response of '%d-%s' was returned when uploading profilePic to S3 bucket.",
                        response.getResponseCode(), response.getDescription()));
    }

    public ImageUploadResponse uploadFileByUserProfile(MultipartFile multipartFile, Long userId, String authToken) {
        ImageUploadResponse invalidSizeResponse = processFileUploadsExceedingLimit(multipartFile);
        if (invalidSizeResponse != null) {
            return invalidSizeResponse;
        }

        Object getResponseObject= doGetRequest(userId, null, authToken, "userProfiles");
        UserProfile userProfile = null;
        try {
            userProfile = UserProfile.createFromObject(getResponseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (userProfile == null || !userProfile.getId().equals(userId)) {
            LOGGER.error("User not found or user profile ids do not match.");
            return createImageUploadResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }

        String fileName = generateFileNameByUserProfile(userId, userProfile.getFirstName());
        ImageUploadResponse response = uploadFileToS3(multipartFile, fileName);
        if (response.getResponseCode() == 200) {
            HttpStatus postResponseStatus = doPostRequest(userId, null, authToken, response.getImageUrl());
            if (postResponseStatus.is2xxSuccessful()) {
                return createImageUploadResponse(true, response.getImageUrl(), HttpStatus.OK, DEFAULT_DESCRIPTION);
            }
            return createImageUploadResponse(false, response.getImageUrl(), postResponseStatus,
                    String.format("Error response %d returned when updating profilePic for userId=%d",
                            postResponseStatus.value(), userId));
        }

        return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                String.format("Failure response of '%d-%s' was returned when uploading profilePic to S3 bucket.",
                        response.getResponseCode(), response.getDescription()));
    }

    public ImageUploadResponse uploadFileByRequest(MultipartFile multipartFile, ImageUploadRequest request) {
        String fileName = generateFileNameByUserAndMatchProfileId(request.getMatchProfile().getUserProfile().getId(),
                request.getMatchProfile().getId());
        return uploadFileToS3(multipartFile, fileName);
    }

    private static File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() != null) {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            if (convFile.length() <= MAX_IMAGE_BYTES) {
                return convFile;
            }
            return Utils.getScaledFile(convFile);
        }
        return null;
    }

    private String generateFileNameByUserAndMatchProfileId(Long userId, Long matchProfileId) {
        return "user_" + userId + "_match_" + matchProfileId + "_" + Utils.getIsoFormatTimestampFromDate(new Date(), null);
    }

    private String generateFileNameByUserProfile(Long userId, String name) {
        return "user_" + userId + "_" + name.toLowerCase() + "_" + Utils.getIsoFormatTimestampFromDate(new Date(), null);
    }

    public ImageUploadResponse deleteUserProfileImage(Long userId, String authToken) {
        //Use http client to hit GET endpoint in userProfileController to verify userProfile exists
        Object getResponseObj = doGetRequest(userId, null, authToken, "userProfiles");
        UserProfile userProfile = null;
        try {
            userProfile = UserProfile.createFromObject(getResponseObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (userProfile == null) {
            return createImageUploadResponse(false, null, NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        else if (StringUtils.isNullOrEmpty(userProfile.getProfileImage())) {
            return createImageUploadResponse(true, null, OK, DEFAULT_DESCRIPTION);
        }

        deleteProfileImageFromS3Bucket(userProfile.getProfileImage());

        return deleteProfileImageUrlFromDatabaseTable(userId, null, authToken);
    }

    public ImageUploadResponse deleteMatchProfileImage(Long userId, Long matchProfileId, String authToken) {
        //Use http client to hit GET endpoint in matchProfileController to verify matchProfile exists
        Object getResponseObject= doGetRequest(userId, matchProfileId, authToken, "matchProfiles");
        MatchProfile matchProfile = null;
        try {
            matchProfile = MatchProfile.createFromObject(getResponseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (matchProfile == null || !matchProfile.getUserProfile().getId().equals(userId)) {
            LOGGER.error("Either matchProfileId={} was not found, or userProfileId contained in the matchProfile result " +
                            "corresponding to matchProfileId={} does not match the path userProfileId={}",
                    matchProfileId, matchProfileId, userId);

            return createImageUploadResponse(false, null, HttpStatus.BAD_REQUEST,
                    String.format("Error deleting profile image: either matchProfile with id=%d is not a valid matchProfile" +
                            " or endpoint URL is malformed.", matchProfileId));
        }
        else if (StringUtils.isNullOrEmpty(matchProfile.getProfileImage())) {
            return createImageUploadResponse(true, null, OK, DEFAULT_DESCRIPTION);
        }
        deleteProfileImageFromS3Bucket(matchProfile.getProfileImage());

        return deleteProfileImageUrlFromDatabaseTable(userId, matchProfileId, authToken);
    }

    /**
     * Makes update to corresponding profile controller endpoint to reset the profile image url to an empty string.
     * @param userId
     * @param matchProfileId
     * @param authToken
     * @return
     */
    private ImageUploadResponse deleteProfileImageUrlFromDatabaseTable(Long userId, Long matchProfileId, String authToken) {
        HttpStatus responseStatus = doPostRequest(userId, matchProfileId, authToken, "");
        if (responseStatus.isError()) {
            return createImageUploadResponse(false, null, HttpStatus.BAD_REQUEST,
                    String.format("Error deleting profile image for matchProfile=%d", matchProfileId));
        }
        return createImageUploadResponse(true, null, OK, "Profile image successfully deleted.");
    }

    private void deleteProfileImageFromS3Bucket(String profileImage) {
        String fileName = profileImage.substring(profileImage.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName)); //Delete from S3 bucket
    }

    private HttpStatus doPostRequest(Long userId, Long matchProfileId, String authToken, String imageUrl) {
        String postEndpoint = matchProfileId == null ?
                String.format(USER_PROFILE_IMAGE_UPDATE_ENDPOINT, userId, imageUrl) :
                String.format(MATCH_PROFILE_IMAGE_UPDATE_ENDPOINT, userId, matchProfileId, imageUrl);

        HttpPost httpPost = new HttpPost(baseUrl + postEndpoint);
        LOGGER.info("Making HTTP POST Request to '{}'", httpPost.getURI());
        httpPost.setHeader("Authorization", authToken);
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            return handlePostResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    private Object doGetRequest(Long userId, Long matchProfileId, String authToken, String getObject) {
        String getEndpoint = matchProfileId == null ?
                String.format(USER_PROFILE_GET_ENDPOINT, userId) :
                String.format(MATCH_PROFILE_GET_ENDPOINT, userId, matchProfileId);
        HttpGet httpGet = new HttpGet(baseUrl + getEndpoint);

        LOGGER.info("Making HTTP GET Request to '{}'", httpGet.getURI());
        httpGet.setHeader("Authorization", authToken);

        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            return handleGetResponse(response, getObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpStatus handlePostResponse(HttpResponse response) {
        if (response == null) {
            return UNPROCESSABLE_ENTITY;
        }
        String responseBody = null;
        try {
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractHttpStatusFromResponse(responseBody);
    }

    private Object handleGetResponse(HttpResponse response, String objectEntityName) throws IOException {
        String responseBody = EntityUtils.toString(response.getEntity());
        HttpStatus httpStatus = extractHttpStatusFromResponse(responseBody);
        if (httpStatus.is2xxSuccessful()) {
            //Extract value for 'status' key from JSON string containing response, because generic HttpResponse will always return 200
            return extractEntityObjectFromResponse(responseBody, objectEntityName);
        }
        return null;
    }

    private HttpStatus extractHttpStatusFromResponse(String responseBodyString) {
        if (StringUtils.isNullOrEmpty(responseBodyString)) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
        BasicJsonParser parser = new BasicJsonParser();
        Map<String, Object> responseMap = parser.parseMap(responseBodyString);
        return HttpStatus.valueOf(responseMap.get("status").toString());
    }

    private Object extractEntityObjectFromResponse(String responseBodyString, String entityName) {
        if (!StringUtils.isNullOrEmpty(responseBodyString)) {
            BasicJsonParser parser = new BasicJsonParser();
            Map<String, Object> responseMap = parser.parseMap(responseBodyString);

            ArrayList<String> entityList =
                    (ArrayList<String>) responseMap.getOrDefault(entityName, null);
            // for single DTO lookup response gives array containing single DTO object
            return entityList == null || entityList.isEmpty() ? null : entityList.get(0);
        }
        return null;
    }

    private ImageUploadResponse processFileUploadsExceedingLimit(MultipartFile file) {
        if (file.getSize() > MAX_IMAGE_BYTES) {
            return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("Error: image exceeds allowable upload size of %d bytes -- file is %d bytes",
                            MAX_IMAGE_BYTES, file.getSize()));
        }
        return null;
    }
}
