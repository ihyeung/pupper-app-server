package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.UserAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepo extends PagingAndSortingRepository<UserAccount, Long> {

    UserAccount findByUsername(String username);
}
