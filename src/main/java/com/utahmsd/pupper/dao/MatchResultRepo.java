package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.MatchResult;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MatchResultRepo extends PagingAndSortingRepository<MatchResult, Long> {
}
