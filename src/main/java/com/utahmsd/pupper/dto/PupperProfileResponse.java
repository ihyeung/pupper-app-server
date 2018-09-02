package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.Pupper;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileResponse extends ProfileResponse {

    @JsonProperty("pupperProfile")
    private Pupper pupper;

    public PupperProfileResponse(ProfileRequest request) {
        super(request);
    }

    public PupperProfileResponse(Long id, String error) {
        super();
        this.setPupper(null);
        this.setId(id);
        this.setStatus(error);
        this.setSuccess(false);
    }

    public Pupper getPupper() {
        return pupper;
    }

    public void setPupper(Pupper pupper) {
        this.pupper = pupper;
    }
}
