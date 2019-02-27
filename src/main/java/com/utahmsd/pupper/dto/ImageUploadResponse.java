package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ImageUploadResponse extends BaseResponse {

    @JsonProperty("imageUrl")
    private String imageUrl;

    public ImageUploadResponse() { }

    @Override
    public <T extends BaseResponse> T createResponse(boolean success, List<PupperEntity> entityList, HttpStatus code, String description) {
        return null;
    }

    public static ImageUploadResponse createImageUploadResponse(boolean success, String imageUrl, HttpStatus code,
                                                                  String description) {
        ImageUploadResponse response = new ImageUploadResponse();
        response.setSuccess(success);
        response.setImageUrl(imageUrl);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
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
