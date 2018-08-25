package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PupperProfileRequest extends ProfileRequest {

    @JsonProperty("IsNew")
    private boolean isNew;

    @JsonProperty("Pupper")
    private Pupper pupper;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Pupper getPupper() {
        return pupper;
    }

    public void setPupper(Pupper pupper) {
        this.pupper = pupper;
    }
}
