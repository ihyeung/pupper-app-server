package com.utahmsd.pupper.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.ImageUploadRequest;
import com.utahmsd.pupper.dto.ImageUploadResponse;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Named
@Singleton
public class AmazonAwsClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void buildClient() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public ImageUploadResponse uploadFile(MultipartFile multipartFile, ImageUploadRequest request) {

        String fileUrl = null;
        ImageUploadResponse response = new ImageUploadResponse();
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(request);
            fileUrl = String.format("%s/%s/%s", endpointUrl, bucketName, fileName);
            uploadFileToS3bucket(fileName, file);
            file.delete();
            response.setSuccess(true);
            response.setImageUrl(fileUrl.replace("us-east-2.", ""));
        } catch (Exception e) {
            response.setSuccess(false);
            e.printStackTrace();
        }
        return response;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(ImageUploadRequest request) {
        UserProfile userProfile = request.getMatchProfile().getUserProfile();
        return userProfile.getId() + "_" + userProfile.getFirstName() + "-" + userProfile.getLastName().substring(0,1) + "_"
                + request.getMatchProfile().getId() + "_" + new Date().getTime();
    }

    private void uploadFileToS3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public ImageUploadResponse deleteFileFromS3Bucket(ImageUploadRequest imageUploadRequest) {
        String imageUrl = imageUploadRequest.getMatchProfile().getProfileImage();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        ImageUploadResponse response = new ImageUploadResponse(imageUrl);
        response.setSuccess(true);

        return response;
    }

}
