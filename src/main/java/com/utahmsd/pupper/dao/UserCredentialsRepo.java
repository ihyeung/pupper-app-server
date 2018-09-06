package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserCredentials;
import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepo extends CrudRepository<UserCredentials, Long> {
}
