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
import static com.utahmsd.pupper.util.ValidationUtils.isValidEmail;
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

    public UserAuthenticationResponse findUserAccountById(Long userAccountId) {
        Optional<UserAccount> result = userAccountRepo.findById(userAccountId);
        if (!result.isPresent()) {
            LOGGER.error("User account with id='{}' not found", userAccountId);
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format("User account with id = %d not found", userAccountId));
        }

        return createUserAuthResponse(true, new ArrayList<>(Arrays.asList(result.get())), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse findUserAccountByEmail(String email) {
        if (!isValidEmail(email)) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.UNPROCESSABLE_ENTITY, INVALID_INPUT);
        }
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(EMAIL_NOT_FOUND, "User account", email));
        }

        return createUserAuthResponse(true, new ArrayList<>(Arrays.asList(result)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse createUserAccount(UserAccount userAccount) {
        if (userAccount.getUsername() == null || !isValidEmail(userAccount.getUsername())) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.UNPROCESSABLE_ENTITY, INVALID_INPUT);
        }
        if (loadUserByUsername(userAccount.getUsername()) != null) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.CONFLICT,
                    "An account with that username already exists.");
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        UserAccount savedUser = userAccountRepo.save(userAccount);
        return createUserAuthResponse(true, new ArrayList<>(Arrays.asList(savedUser)), HttpStatus.OK, DEFAULT_DESCRIPTION);

//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setUser(request.getUser());
//        String salt = generateSaltString(NUM_SALT_BYTES);
//        userCredentials.setSalt(salt);
//        userCredentials.setDateJoined(Date.valueOf(LocalDate.now()));
//        userCredentials.setComputedHash(generateHash(request.getUser().getPassword(), salt.getBytes()));
//        UserCredentials userCredentialsResult = userAccountRepo.save(userCredentials);
    }

    public UserAuthenticationResponse updateUserAccountById(Long accountId, UserAccount userAccount) {
        //First check to make sure email is registered as an existing account, and that the path param id and the request body id match
        if (loadUserByUsername(userAccount.getUsername()) == null || accountId != userAccount.getId()) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.BAD_REQUEST,
                    String.format(EMAIL_NOT_FOUND, "User account", userAccount.getUsername()));
        }
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        userAccountRepo.save(userAccount);

        return createUserAuthResponse(true, new ArrayList<>(Arrays.asList(userAccount)), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse updateUserAccountByEmail(String email, UserAccount userAccount) {
        if (!userAccount.getUsername().equals(email)) {
            LOGGER.error("Email does not match.");
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND, INVALID_PATH_VARIABLE);
        }
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    String.format(EMAIL_NOT_FOUND, "User account", email));
        }
        userAccountRepo.save(userAccount);
        return createUserAuthResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse deleteUserAccountById(Long accountId) {
        Optional<UserAccount> result = userAccountRepo.findById(accountId);
        if (!result.isPresent()) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND,
                    "Invalid deleteCredentialsById request.");
        }
        userAccountRepo.deleteById(accountId);
        return createUserAuthResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

    public UserAuthenticationResponse deleteUserAccountByEmail(String email) {
        UserAccount result = userAccountRepo.findByUsername(email);
        if (result == null) {
            LOGGER.error(String.format(EMAIL_NOT_FOUND, "User account", email));
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        userAccountRepo.deleteById(result.getId());
        return createUserAuthResponse(true, emptyList(), HttpStatus.OK, DEFAULT_DESCRIPTION);
    }

//    public UserAuthenticationResponse authenticateUser(UserCredentialsRequest request) {
//        String email = request.getUser().getUsername();
//        String password = request.getUser().getPassword();
//        Optional<UserCredentials> result = userAccountRepo.findByEmail(email);
//        if (!result.isPresent()) {
//            LOGGER.error(String.format(INVALID_EMAIL, email));
//
//            return createUserCredentialsResponse(false, Collections.emptyList(), HttpStatus.NOT_FOUND, String.format(INVALID_EMAIL, email));
//        }
//        ArrayList<User> user = new ArrayList<>();
//        user.add(new User(email, null));
//
//        UserCredentials userCredentials = result.get();
//        String computedHashFromSubmittedPassword = generateHash(password, userCredentials.getSalt().getBytes());
//        if (userCredentials.getComputedHash().equals(computedHashFromSubmittedPassword)) { //Login success
//
//            return createUserCredentialsResponse(true, user, HttpStatus.OK, DEFAULT_DESCRIPTION);
//        }
//        //Otherwise, invalid login credentials
//        return createUserCredentialsResponse(false, user, HttpStatus.UNAUTHORIZED, INVALID_LOGIN);
//        return null;

//    }

    public UserAuthenticationResponse getUserAccountByUserProfileId(Long userProfileId) {
        Optional<UserProfile> userProfileResult = userProfileRepo.findById(userProfileId);
        if (!userProfileResult.isPresent()) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        Long userAccountId = userProfileResult.get().getUserAccount().getId();
        Optional<UserAccount> userAccountResult = userAccountRepo.findById(userAccountId);
        if (!userAccountResult.isPresent()) {
            return createUserAuthResponse(false, emptyList(), HttpStatus.NOT_FOUND, String.format(
                    "Invalid getUserAccountByUserProfileId request -- " +
                            "no valid UserAccount is associated with userProfileId {}.", userProfileId));
        }
        return createUserAuthResponse(true, new ArrayList<>(Arrays.asList(userAccountResult.get())),
                HttpStatus.OK, DEFAULT_DESCRIPTION);
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
