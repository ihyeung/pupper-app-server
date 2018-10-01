package com.utahmsd.pupper.service.filter;

import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;

@Named
@Singleton
public class UserSearchFilterService {

    private final int DEFAULT_PAGE_NUM = 0;
    private final int DEFAULT_PAGE_SIZE = 10;
    private final String DEFAULT_SORT_ORDER = "lastName";

    public static final Logger LOGGER = LoggerFactory.getLogger(UserSearchFilterService.class);


    @Inject
    UserProfileRepo userProfileRepo;

    public UserProfileResponse filterUserProfilesByZip(String zipCode) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, DEFAULT_SORT_ORDER));
        Page<UserProfile> results = userProfileRepo.findByZip(zipCode, PageRequest.of(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, sort));
        long numResults = results.getTotalElements();
        LOGGER.info("Number of results with zipcode {}: {}", zipCode,  numResults);
        if (numResults > 0) {
            return UserProfileResponse.createUserProfileResponse(true, results.getContent(), HttpStatus.OK);
        }
        return UserProfileResponse.createUserProfileResponse(true, Collections.emptyList(), HttpStatus.NO_CONTENT);
    }
}
