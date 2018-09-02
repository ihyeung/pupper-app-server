package com.utahmsd.pupper.dao;

import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepo extends CrudRepository<UserAccount, Long> {
}
