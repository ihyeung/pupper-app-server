package com.utahmsd.pupper.service.filter;

import com.amazonaws.util.StringUtils;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.utahmsd.pupper.dao.entity.MatchProfile.matchProfileFieldList;
import static com.utahmsd.pupper.util.Constants.*;

@Service
public class MatchProfileFilterService {

    private static final String DEFAULT_SORT = "score";
    private static final Sort.Direction SORT_DIRECTION = Sort.Direction.DESC; //Sort matchProfiles by descending score

    private final MatchProfileRepo matchProfileRepo;

    @Autowired
    public MatchProfileFilterService(MatchProfileRepo matchProfileRepo) {
        this.matchProfileRepo = matchProfileRepo;
    }

    public List<MatchProfile> getMatchProfilesWithLastLoginSortLimitFilters(String sort, String limit) {
        int resultSize = Integer.valueOf(limit) > PAGE_SIZE ? PAGE_SIZE : Integer.valueOf(limit);
        String sortBy = isValidMatchProfileSort(sort) ? sort : DEFAULT_SORT;
        Sort sortCriteria = new Sort(new Sort.Order(SORT_DIRECTION, sortBy));

        PageRequest pageRequest = PageRequest.of(PAGE_NUM, resultSize, sortCriteria);
        return matchProfileRepo.findMatchProfilesWithRecentActivityOrderByScore(RECENT_ACTIVITY_CUTOFF, pageRequest);
    }

    public List<MatchProfile> getMatchProfilesFilterByUserProfileZip(String zip) {
        return matchProfileRepo.findAllByZip(zip);
    }

    public List<MatchProfile> getMatchProfilesFilterByUserEmail(String email) {
        return matchProfileRepo.findAllByUserEmail(email);
    }

    public List<MatchProfile> getMatchProfilesFilterByBreed(String breed) {
        return matchProfileRepo.findMatchProfilesByBreed_Name(breed);
    }

    public static boolean isValidMatchProfileSort(String sort) {
        return !StringUtils.isNullOrEmpty(sort) && matchProfileFieldList().contains(sort);
    }

}
