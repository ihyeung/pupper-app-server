package com.utahmsd.pupper.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.BaseResponse;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import com.utahmsd.pupper.util.Utils;
import org.imgscalr.Scalr;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.amazonaws.regions.Regions.US_EAST_2;
import static com.utahmsd.pupper.dto.BaseResponse.successResponse;

@Named
@Singleton
public class AmazonAwsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonAwsClient.class);
    private static final String AWS_REGION = "us-east-2";

    @Inject
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;

//    @Value("${amazonProperties.accessKey}")
//    private String accessKey;
//    @Value("${amazonProperties.secretKey}")
//    private String secretKey;

//    @PostConstruct
//    private void buildClient() {
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//        this.s3client = new AmazonS3Client(credentials);
//    }

    private ImageUploadResponse uploadFileToS3(MultipartFile file, String fileName) {
        String outputFilePath = String.format("%s/%s/%s", endpointUrl, bucketName, fileName);
        String imageUrl = outputFilePath.replace(US_EAST_2 + ".", "");
        ImageUploadResponse response = ImageUploadResponse.successResponse(imageUrl);

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
            LOGGER.error("File upload to S3 bucket failed: {}", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public ImageUploadResponse uploadFileByUserAndMatchProfile(MultipartFile multipartFile, Long userId, Long matchProfileId) {
        String fileName = generateFileNameByUserAndMatchProfileId(userId, matchProfileId);
        return uploadFileToS3(multipartFile, fileName);
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
//        UserProfile userProfile = request.getMatchProfile().getUserProfile();
//        return userProfile.getId() + "_" + userProfile.getFirstName() + "-" + userProfile.getLastName().substring(0,1) + "_"
//                + request.getMatchProfile().getId() + "_" + new Date().getTime();
//    }

    public ImageUploadResponse deleteFileFromS3Bucket(ImageUploadRequest imageUploadRequest) {
        String imageUrl = imageUploadRequest.getMatchProfile().getProfileImage();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));

        return ImageUploadResponse.successResponse(imageUrl);
    }



}
