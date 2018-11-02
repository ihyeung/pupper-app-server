package com.utahmsd.pupper.service;

import com.utahmsd.pupper.dao.UserAccountRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dto.UserAuthenticationRequest;
import com.utahmsd.pupper.dto.UserAuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.utahmsd.pupper.util.Constants.DEFAULT_DESCRIPTION;
import static java.util.Collections.emptyList;

@Named
@Singleton
public class UserAccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);
    private static final String INVALID_CREDENTIALS_ID = "User credentials with id %d not found";
    private static final String INVALID_EMAIL = "User credentials with email %s not found";
    private static final String INVALID_LOGIN = "Invalid login credentials were entered.";

    private static final int NUM_SALT_BYTES = 32;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserAccountRepo userAccountRepo;

    private final UserProfileRepo userProfileRepo;

    @Autowired
    public UserAccountService(BCryptPasswordEncoder bCryptPasswordEncoder, UserAccountRepo userAccountRepo, UserProfileRepo userProfileRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
    }

    public List<UserAccount> getAllUserCredentials() {
        Iterable<UserAccount> resultList = userAccountRepo.findAll();
        List<UserAccount> userList = new ArrayList<>();
        if (resultList.iterator().hasNext()) {
            resultList.forEach(each -> userList.add(
                    new UserAccount(each.getUsername(), null))); //Hide sensitive data field
        }
//
//            return createUserCredentialsResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
            return userList;
//        }
//
//        return createUserCredentialsResponse(
//                false, userList, HttpStatus.NO_CONTENT, "No user credentials were found.");
    }

    public UserAccount findUserCredentials(Long userId) {
        Optional<UserAccount> result = userAccountRepo.findById(userId);
//        List<User> userList = new ArrayList<>();
        if (result.isPresent()) {
            LOGGER.error("User credentials with id {} not found", userId);

            return result.get();

        }
//            userList.add(new User(result.get().getUser().getUsername(), null)); //Hide sensitive data field
//            return createUserCredentialsResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
//        }
//        LOGGER.error("User credentials with id {} not found", userId);
//
//        return createUserCredentialsResponse(
//                false, userList, HttpStatus.NOT_FOUND, String.format(INVALID_CREDENTIALS_ID, userId));
        return null;

    }

    public UserAuthenticationResponse createUserCredentials(UserAuthenticationRequest request) {
        UserAccount userAccount = request.getUser();
        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        System.out.println(userAccount.getPassword());
        userAccountRepo.save(userAccount);
//        request.getUser()
//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setUser(request.getUser());
//        String salt = generateSaltString(NUM_SALT_BYTES);
//        userCredentials.setSalt(salt);
//        userCredentials.setDateJoined(Date.valueOf(LocalDate.now()));
//        System.out.println("PASSWORD CLEARTEXT: " + userCredentials.getUser().getPassword());
//        System.out.println("PASSWORD ENCRYPTED: " + bCryptPasswordEncoder.encode(userCredentials.getUser().getPassword()));
//
//        userCredentials.setComputedHash(generateHash(request.getUser().getPassword(), salt.getBytes()));
//        UserCredentials userCredentialsResult = userAccountRepo.save(userCredentials);
        ArrayList<UserAccount> userList = new ArrayList<>();
        userList.add(
                new UserAccount(userAccount.getUsername(), null)); //Hide sensitive data field
        return UserAuthenticationResponse.createUserAuthResponse(true, userList, HttpStatus.OK, DEFAULT_DESCRIPTION);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userResult = userAccountRepo.findByUsername(username);
        if (userResult == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                userResult.getUsername(),
                userResult.getPassword(),
                emptyList());
    }

}
