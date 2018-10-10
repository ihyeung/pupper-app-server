package com.utahmsd.pupper.controller;

import com.utahmsd.pupper.client.AmazonAwsClient;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

@RestController
@Api(value = "Image Controller")
@RequestMapping("/storage")
public class ImageController {

    private final AmazonAwsClient amazonClient;

    @Inject
    ImageController(AmazonAwsClient amazonAwsClient) {
        amazonClient = amazonAwsClient;
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ImageUploadResponse uploadFile(@RequestPart(value = "profilePic") MultipartFile file,
                                          @RequestPart(value = "requestBody") ImageUploadRequest imageUploadRequest) {
        return this.amazonClient.uploadFile(file, imageUploadRequest);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public ImageUploadResponse deleteFile(@RequestBody ImageUploadRequest imageUploadRequest) {
        return this.amazonClient.deleteFileFromS3Bucket(imageUploadRequest);
    }

}
