package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.AmazonAwsClient;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static com.utahmsd.pupper.dto.ImageUploadResponse.createImageUploadResponse;
import static com.utahmsd.pupper.util.Constants.NULL_FIELD;

/**
 * Controller specifically for setting/updating the profile image for a given matchProfile, through the use of uploading/deleting
 * profile image files to an S3 bucket in AWS.
 *
 * Currently a maximum of one photo can be uploaded per matchProfile.
 * TODO: Extend this to allow multiple profile pictures for a matchProfile, allow a profile picture for a userProfile.
 */

@RestController
@Api(value = "Image Controller")
public class ImageController {

    private final AmazonAwsClient amazonClient;

    @Autowired
    public ImageController(AmazonAwsClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    /*
    Generic upload endpoint using a RequestBody.
    The imageUploadRequest MUST contain a matchProfile object with a nested userProfile object.
     */
    @PostMapping(path = "/upload", consumes = {"multipart/form-data"}, produces = "application/json")
    public ImageUploadResponse uploadFile(@RequestPart(value = "profilePic") MultipartFile file,
                                          @RequestPart(value = "requestBody") @RequestBody ImageUploadRequest imageUploadRequest) {
        if (imageUploadRequest.getMatchProfile() == null || imageUploadRequest.getMatchProfile().getUserProfile() == null) {
            return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format(NULL_FIELD, "matchProfile and/or userProfile"));
        }

        return this.amazonClient.uploadFileByRequest(file, imageUploadRequest);
    }

    /**
     * Upload endpoint that references the matchProfileId and userProfileId in the endpoint path.
     * No JSON request body is needed.
     *
     * A 500 error response is returned when the request is rejected due to no multipart boundary being
     * found (i.e., no profilePic file was included in the request).
     *
     * @param file form-data for profilePic to upload
     * @param userId the userProfileId that the upload will correspond to
     * @param matchProfileId the matchProfileId that the uploaded file will belong to
     * @param authToken authorization token for accessing endpoint called in AmazonAwsClient class.
     * @return imageUploadResponse
     */
    @PutMapping(path = "/upload/user/{userId}/matchProfile/{matchProfileId}", consumes = {"multipart/form-data"}, headers = {"Authorization"})
    public ImageUploadResponse uploadProfileImageForMatchProfile(@RequestPart(value = "profilePic") @NotNull MultipartFile file,
                                                         @PathVariable("userId") Long userId,
                                                         @PathVariable("matchProfileId") Long matchProfileId,
                                                         @RequestHeader("Authorization") String authToken) {

        return amazonClient.uploadFileByUserAndMatchProfile(file, userId, matchProfileId, authToken);
    }

    @PutMapping(path = "/upload/user/{userId}", consumes = {"multipart/form-data"}, headers = {"Authorization"})
    public ImageUploadResponse uploadProfileImageForUserProfile(@RequestPart(value = "profilePic") @NotNull MultipartFile file,
                                                         @PathVariable("userId") Long userId,
                                                         @RequestHeader("Authorization") String authToken) {
        return amazonClient.uploadFileByUser(file, userId, authToken);
    }

    /*
    Delete endpoint for deleting a profile picture for a given userId/matchProfileId.
     */
    @DeleteMapping(path = "/user/{userId}/matchProfile/{matchProfileId}/upload", headers = {"Authorization"})
    public ImageUploadResponse deleteFile(@PathVariable("userId") Long userId,
                                          @PathVariable("matchProfileId") Long matchProfileId,
                                          @RequestHeader("Authorization") String authToken) {
        return this.amazonClient.deleteFileFromS3Bucket(userId, matchProfileId, authToken);
    }
}




