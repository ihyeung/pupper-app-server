package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchPreference;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchPreferenceRepo extends PagingAndSortingRepository<MatchPreference, Long> {
}
