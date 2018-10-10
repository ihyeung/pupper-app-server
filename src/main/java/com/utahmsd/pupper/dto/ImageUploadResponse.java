package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.MatchProfile;

public class ImageUploadResponse extends BaseResponse {

    @JsonProperty("imageUrl")
    private String imageUrl;

    public ImageUploadResponse() {
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
