package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.ProfileRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRequestRepo extends CrudRepository<ProfileRequest, Long> {

}
