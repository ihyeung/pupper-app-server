package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {

//    UserAccount save(UserAccount userAccount);
    UserAccount findByUsername(String username);
//    Optional<UserAccount> findById(Long id);
//    Iterable<UserAccount> findAll();
//    Iterable<UserAccount> findAll(Sort sort);
//    void deleteById(Long id);

}
