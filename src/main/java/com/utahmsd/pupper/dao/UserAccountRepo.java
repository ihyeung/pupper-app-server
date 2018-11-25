package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserAccountRepo extends PagingAndSortingRepository<UserAccount, Long> {
//    UserAccount save(UserAccount userAccount);
    UserAccount findByUsername(String username);
//    Iterable<UserAccount> findAll();
//    Iterable<UserAccount> findAll(Sort sort);
//    void deleteById(Long id);
}
