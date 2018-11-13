package com.utahmsd.pupper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utahmsd.pupper.dao.entity.Breed;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BreedResponse extends BaseResponse {

    @JsonProperty("pupperBreeds")
    private List<Breed> breedList;

    public BreedResponse() {
    }

    public static BreedResponse createBreedResponse(boolean success, List<Breed> breeds, HttpStatus code,
                                                    String description) {
        BreedResponse response = new BreedResponse();
        response.setSuccess(success);
        response.setBreedList(breeds);
        response.setStatus(code);
        response.setResponseCode(code.value());
        response.setDescription(description);
        return response;
    }

    public List<Breed> getBreedList() {
        return breedList;
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
    }
}
