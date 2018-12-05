package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserAccountRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.dto.UserAuthenticationResponse.createUserAuthResponse;
import static com.utahmsd.pupper.util.Constants.*;
import static java.util.Collections.emptyList;

@Named
@Singleton
public class UserAccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserAccountRepo userAccountRepo;
    private final UserProfileRepo userProfileRepo;

    @Autowired
    public UserAccountService(BCryptPasswordEncoder bCryptPasswordEncoder, UserAccountRepo userAccountRepo,
                              UserProfileRepo userProfileRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
    }

    public UserAuthenticationResponse getAllUserAccounts() {
        Iterable<UserAccount> resultList = userAccountRepo.findAll();
        List<UserAccount> userList = new ArrayList<>();
        if (resultList.iterator().hasNext()) {
            resultList.forEach(userList::add);
        }
        return createUserAuthResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse findUserAccountByUserAccountId(Long userAccountId) {
        Optional<UserAccount> result = userAccountRepo.findById(userAccountId);
        return !result.isPresent() ?
                createUserAuthResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format("User account with id = %d not found", userAccountId)) :
                createUserAuthResponse(true, Arrays.asList(result.get()), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse findUserAccountByEmail(String email) {
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));

            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMAIL_NOT_FOUND, "User account", email));
        }

        return createUserAuthResponse(true, Arrays.asList(result), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse getUserAccountByUserProfileId(Long userProfileId) {
        Optional<UserProfile> userProfileResult = userProfileRepo.findById(userProfileId);
        if (!userProfileResult.isPresent()) {
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        Long userAccountId = userProfileResult.get().getUserAccount().getId();
        Optional<UserAccount> userAccountResult = userAccountRepo.findById(userAccountId);
        if (!userAccountResult.isPresent()) {
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(ID_NOT_FOUND.replace("id", "user profile id"), "User account", userProfileId));
        }
        return createUserAuthResponse(true, Arrays.asList(userAccountResult.get()),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse registerNewUserAccount(UserAccount userAccount) {
        UserAccount result = userAccountRepo.findByUsername(userAccount.getUsername());
        if (result != null) {
            return createUserAuthResponse(false, null, HttpStatus.CONFLICT,
                    "User account already exists.");
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        UserAccount savedUser = userAccountRepo.save(userAccount);
        return createUserAuthResponse(true, Arrays.asList(savedUser), HttpStatus.OK, DEFAULT_DESCRIPTION);

//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setUser(request.getUser());
//        String salt = generateSaltString(NUM_SALT_BYTES);
//        userCredentials.setSalt(salt);
//        userCredentials.setDateJoined(Date.valueOf(LocalDate.now()));
//        userCredentials.setComputedHash(generateHash(request.getUser().getPassword(), salt.getBytes()));
//        UserCredentials userCredentialsResult = userAccountRepo.save(userCredentials);
    }

    public UserAuthenticationResponse updateUserAccountById(Long accountId, UserAccount userAccount) {
        UserAccount result = userAccountRepo.findByUsername(userAccount.getUsername());
        //First check to make sure email is registered as an existing account, and that the path param id and the request body id match
        if (result == null || !accountId.equals(userAccount.getId())) {
            return createUserAuthResponse(false, null, HttpStatus.BAD_REQUEST,
                    String.format(EMAIL_NOT_FOUND, "User account", userAccount.getUsername()));
        }
        userAccount.setId(result.getId());
        UserAccount savedResult = userAccountRepo.save(userAccount);

        return createUserAuthResponse(true, Arrays.asList(savedResult), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse updateUserAccountByEmail(String email, UserAccount userAccount) {
        if (!userAccount.getUsername().equals(email)) {
            LOGGER.error("Email does not match.");
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(EMAIL_NOT_FOUND, "User account", email));
        }
        userAccount.setId(result.getId());
        userAccountRepo.save(userAccount);
        return createUserAuthResponse(true, Arrays.asList(userAccount), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse deleteUserAccountById(Long accountId) {
        Optional<UserAccount> result = userAccountRepo.findById(accountId);
        if (!result.isPresent()) {
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND,
                    String.format(ID_NOT_FOUND, "User account", accountId));
        }
        userAccountRepo.deleteById(accountId);
        return createUserAuthResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse deleteUserAccountByEmail(String email) {
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));
            return createUserAuthResponse(false, null, HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        userAccountRepo.deleteById(result.getId());
        return createUserAuthResponse(true, null, HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount userResult = userAccountRepo.findByUsername(username);
        if (userResult == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", username));
            return null;
        }
        return new org.springframework.security.core.userdetails.User(
                userResult.getUsername(),
                userResult.getPassword(),
                emptyList());
    }

}
