package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.AmazonAwsClient;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

import static com.utahmsd.pupper.dto.ImageUploadResponse.createImageUploadResponse;

@RestController
@Api(value = "Image Controller")
public class ImageController {

    private static final Long MAX_UPLOAD_BYTES = 1_048_576L;

    @Inject
    private AmazonAwsClient amazonClient;

    @PostMapping(path = "/upload", consumes = {"multipart/form-data"}, produces = "application/json")
    public ImageUploadResponse uploadFile(@RequestPart(value = "profilePic") MultipartFile file,
                                          @RequestPart(value = "requestBody") @RequestBody ImageUploadRequest imageUploadRequest) {
        return this.amazonClient.uploadFileByRequest(file, imageUploadRequest);
    }

    @PostMapping(path = "/user/{userId}/matchProfile/{matchProfileId}/upload", consumes = {"multipart/form-data"})
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
    Deletes profile picture for a given userId/matchProfileId
     */
    @DeleteMapping(path = "/user/{userId}/matchProfile/{matchProfileId}/upload")
    public ImageUploadResponse deleteFile(@PathVariable("userId") Long userId,
                                          @PathVariable("matchProfileId") Long matchProfileId) {
        return this.amazonClient.deleteFileFromS3Bucket(userId, matchProfileId);
    }
}




