package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.AmazonAwsClient;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.utahmsd.pupper.dto.ImageUploadResponse.createImageUploadResponse;
import static com.utahmsd.pupper.util.Constants.NOT_FOUND;

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

    private static final Long MAX_UPLOAD_BYTES = 1_048_576L;

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
                    "Null check validation failed");
        }

        return this.amazonClient.uploadFileByRequest(file, imageUploadRequest);
    }

    /*
    Upload endpoint that references the matchProfileId and userProfileId in the endpoint path.
    No JSON request body is needed.
     */
    @PutMapping(path = "/user/{userId}/matchProfile/{matchProfileId}/upload", consumes = {"multipart/form-data"})
    public ImageUploadResponse uploadFileForMatchProfile(@RequestPart(value = "profilePic") MultipartFile file,
                                                         @PathVariable("userId") Long userId,
                                                         @PathVariable("matchProfileId") Long matchProfileId) throws IllegalStateException {
        if (file.getSize() > MAX_UPLOAD_BYTES) {
           return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                   String.format("Error: file exceeds allowable upload size of %d bytes -- file is %d bytes",
                    MAX_UPLOAD_BYTES, file.getSize()));
        }
        return amazonClient.uploadFileByUserAndMatchProfile(file, userId, matchProfileId);
    }

    /*
    Delete endpoint for deleting a profile picture for a given userId/matchProfileId.
     */
    @DeleteMapping(path = "/user/{userId}/matchProfile/{matchProfileId}/upload")
    public ImageUploadResponse deleteFile(@PathVariable("userId") Long userId,
                                          @PathVariable("matchProfileId") Long matchProfileId) {
        return this.amazonClient.deleteFileFromS3Bucket(userId, matchProfileId);
    }
}




