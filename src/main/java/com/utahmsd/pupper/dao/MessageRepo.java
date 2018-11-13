package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.PupperMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepo extends PagingAndSortingRepository<PupperMessage, Long> {
}
