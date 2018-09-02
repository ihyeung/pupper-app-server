package com.utahmsd.pupper.dao;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserCredentialsRepoImpl extends RepositoryImpl<UserAccount, Long> implements UserCredentialsRepo {

    public UserCredentialsRepoImpl() {
        entityClass = UserAccount.class;
    }
}