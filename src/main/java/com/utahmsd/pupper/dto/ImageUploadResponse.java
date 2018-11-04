package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ImageUploadResponse extends BaseResponse {

    @JsonProperty("imageUrl")
    private String imageUrl;

    public ImageUploadResponse() {
    }

    public static ImageUploadResponse errorResponse(HttpStatus status, String description) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.setSuccess(false);
        response.setResponseCode(status.value());
        response.setStatus(status);
        response.setDescription(description);
        return response;

    }

    public static ImageUploadResponse successResponse(String url) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setDescription(String.format("File upload success: %s", url));
        response.setImageUrl(url);
        return response;
    }

    public ImageUploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
