package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dto.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.List;

public class UserCredentialsRepoImpl implements UserCredentialsRepo {

    @Override
    public UserAccount createUserAccount(UserAuthenticationRequest request) {
        UserAccount userAccount = new UserAccount();
        return userAccount.createAccountFromRequest(request);
    }

    @Override
    public UserAccount findById(int id) {
        return null;
    }

    @Override
    public Option<Long> find(UserAccount userAccount) {
        return null;
    }

    @Override
    public Option<List<Long>> findAll() {
        return null;
    }

    @Override
    public Option<Long> save(Long aLong) {
        return null;
    }

    @Override
    public Option<Long> delete(Long aLong) {
        return null;
    }
}
