package com.utahmsd.pupper.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.amazonaws.regions.Regions.US_EAST_1;
import static com.utahmsd.pupper.dto.ImageUploadResponse.createImageUploadResponse;
import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static com.utahmsd.pupper.util.Constants.INVALID_REQUEST;

@Named
@Singleton
public class AmazonAwsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonAwsClient.class);

    private final AmazonS3 s3client;
    private final MatchProfileRepo matchProfileRepo;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Autowired
    public AmazonAwsClient(AmazonS3 s3client, MatchProfileRepo matchProfileRepo) {
        this.s3client = s3client;
        this.matchProfileRepo = matchProfileRepo;
    }

    private ImageUploadResponse uploadFileToS3(MultipartFile file, String fileName) {
        String outputFilePath = String.format("%s/%s/%s", endpointUrl, bucketName, fileName);
        String imageUrl = outputFilePath.replace(US_EAST_1 + ".", "");

        ImageUploadResponse response = createImageUploadResponse(true, imageUrl, HttpStatus.OK, DEFAULT_DESCRIPTION);
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
            response.setSuccess(false);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setDescription(String.format("File upload to S3 bucket failed: '%s'", e.getMessage()));
            response.setImageUrl("");
            LOGGER.error("File upload to S3 bucket failed: {}", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public ImageUploadResponse uploadFileByUserAndMatchProfile(MultipartFile multipartFile, Long userId, Long matchProfileId) {
        Optional<MatchProfile> matchProfileResult = matchProfileRepo.findById(matchProfileId);
        if (!matchProfileResult.isPresent() || !matchProfileResult.get().getUserProfile().getId().equals(userId)) {
            return createImageUploadResponse(false, null, HttpStatus.BAD_REQUEST, INVALID_REQUEST);
        }
        String fileName = generateFileNameByUserAndMatchProfileId(userId, matchProfileId);
        ImageUploadResponse response = uploadFileToS3(multipartFile, fileName);
        if (response.getResponseCode() == 200) {
            MatchProfile updatedMatchProfile = matchProfileResult.get();
            updatedMatchProfile.setProfileImage(response.getImageUrl());
            matchProfileRepo.save(updatedMatchProfile);

            return createImageUploadResponse(true, response.getImageUrl(), HttpStatus.OK, DEFAULT_DESCRIPTION);
        }

        return createImageUploadResponse(false, null, HttpStatus.UNPROCESSABLE_ENTITY,
                String.format("Failure response of '%d-%s' was returned when uploading file to s3 bucket.",
                        response.getResponseCode(), response.getDescription()));
    }


    public ImageUploadResponse uploadFileByRequest(MultipartFile multipartFile, ImageUploadRequest request) {
        String fileName = generateFileNameByUserAndMatchProfileId(request.getMatchProfile().getUserProfile().getId(),
                request.getMatchProfile().getUserProfile().getId());
        return uploadFileToS3(multipartFile, fileName);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        final long MAX_BYTES_LONG = 1048576;
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        if (convFile.length() > MAX_BYTES_LONG) {
            return getScaledFile(MAX_BYTES_LONG, convFile);
        }
        return convFile;
    }

    private File getScaledFile(long MAX_BYTES_LONG, File file) throws IOException {
        LOGGER.error("Error: file being uploaded exceeds maximum allowable bytes size: {}", file.length());
        long scaleFactor = file.length()/MAX_BYTES_LONG;
        BufferedImage image = ImageIO.read(file);
        File outputfile = new File(file.getName() + "-scaled.jpg");
        int height = image.getHeight();
        int width = image.getWidth();
        if (scaleFactor > 2) {
            BufferedImage scaledImage =
                    Scalr.resize(image, Scalr.Method.BALANCED, width/2, height/2);
            LOGGER.info(String.format("Scaled dimensions: %d w x %d h", width/2, height/2));

            try {
                ImageIO.write(scaledImage, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputfile;
        }
        ImageIO.write(image, "jpg", outputfile);

        return outputfile;
    }

    private String generateFileNameByUserAndMatchProfileId(Long userId, Long matchProfileId) {
        return "user_" + userId + "_match_" + matchProfileId + "_" + new Date().getTime();
    }

//    private String generateFileName(ImageUploadRequest request) {
//        UserProfile userProfile = request.getMatchProfile().findUserProfileById();
//        return userProfile.getId() + "_" + userProfile.getFirstName() + "-" + userProfile.getLastName().substring(0,1) + "_"
//                + request.getMatchProfile().getId() + "_" + new Date().getTime();
//    }

    public ImageUploadResponse deleteFileFromS3Bucket(Long userId, Long matchProfileId) {
        Optional<MatchProfile> matchProfileResult = matchProfileRepo.findById(matchProfileId);
        if (!matchProfileResult.isPresent() || !matchProfileResult.get().getUserProfile().getId().equals(userId)) {
            return createImageUploadResponse(false, null, HttpStatus.BAD_REQUEST,
                    String.format("DeleteFileFromS3Bucket Failure: either no photo belonging to userId %d and matchProfileId %d exists," +
                            " or malformed url.", userId, matchProfileId));
        }
        String imageUrl = matchProfileResult.get().getProfileImage();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        //Update the imageUrl field for that user
        matchProfileResult.get().setProfileImage(null);
        matchProfileRepo.save(matchProfileResult.get());

        return createImageUploadResponse(true, imageUrl, HttpStatus.OK, "DeleteFileFromS3Bucket success.");
    }



}
