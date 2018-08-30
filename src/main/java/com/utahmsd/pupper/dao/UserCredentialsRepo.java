package com.utahmsd.pupper.dao;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.utahmsd.pupper.dto.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;

public interface UserCredentialsRepo extends Repository<Long, UserAccount> {
    UserAccount createUserAccount(UserAuthenticationRequest request);
    UserAccount findById(int id);

}
