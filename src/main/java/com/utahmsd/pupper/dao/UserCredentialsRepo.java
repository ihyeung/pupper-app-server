package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserCredentials;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserCredentialsRepo extends Repository<UserCredentials, Long> {
    UserCredentials save(UserCredentials userCredentials);
    Optional<UserCredentials> findByEmail(String email);
    Optional<UserCredentials> findById(Long id);
    void deleteById(Long id);
    void deleteByEmail(String email);
}
