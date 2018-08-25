package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepo extends CrudRepository<Match, Long> {
}
