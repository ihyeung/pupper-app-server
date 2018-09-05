package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepo extends CrudRepository<UserAccount, Long> {
}
