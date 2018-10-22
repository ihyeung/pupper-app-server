package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserCredentials;
import com.utahmsd.pupper.dto.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserCredentialsRepo extends PagingAndSortingRepository<UserCredentials, Long> {
    UserCredentials save(UserCredentials userCredentials);
    User save(User user);
    Optional<UserCredentials> findByEmail(String email);
    Optional<UserCredentials> findById(Long id);
    Iterable<UserCredentials> findAll();
    Iterable<UserCredentials> findAll(Sort sort);

    void deleteById(Long id);
    void deleteByEmail(String email);
}
