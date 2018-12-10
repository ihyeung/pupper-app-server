package com.utahmsd.pupper;

import com.utahmsd.pupper.dao.entity.*;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.Gender;
import com.utahmsd.pupper.dto.pupper.MaritalStatus;
import com.utahmsd.pupper.dto.pupper.Size;
import com.utahmsd.pupper.util.PupperUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TestUtils {
    
    public static UserAccount createUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("createUserProfileTest@test.com");
        userAccount.setPassword("TESTPASSWORD");
        userAccount.setId(3L);

        return userAccount;
    }

    public static UserProfile createUserProfile(UserAccount userAccount) {
        UserProfile user = new UserProfile();
        user.setUserAccount(userAccount);
        user.setFirstName("Carmen");
        user.setLastName("San Diego");
        user.setBirthdate(Date.from(Instant.parse("2004-12-03T10:15:30.00Z")));
        user.setZip("84095");
        user.setSex(Gender.FEMALE);
        user.setMaritalStatus(MaritalStatus.SINGLE);
        user.setLastLogin(Date.from(Instant.now()));
        user.setDateJoin(Date.from(Instant.now()));

        return user;
    }

    public static MatchProfile createMatchProfile(UserProfile userProfile) {
        MatchProfile matchProfile = new MatchProfile();
        matchProfile.setUserProfile(userProfile);
        matchProfile.setNames("Milo pie and Lulu");
        Breed breed = new Breed();
        breed.setName("Yorkshire Terrier");
        matchProfile.setBreed(breed);
        matchProfile.setAboutMe("Two tiny pups with a lot of attitude");
        matchProfile.setSize(Size.TOY);
        matchProfile.setEnergyLevel(Energy.LOW);
        matchProfile.setBirthdate(Date.from(Instant.now().minus(600L, ChronoUnit.DAYS)));
        matchProfile.setLifeStage(PupperUtils.dobToLifeStage(matchProfile.getBirthdate()));
        matchProfile.setNumDogs(2);
        matchProfile.setSex(Gender.FEMALE);

        return matchProfile;
    }

    public static PupperProfile createPupperProfile(MatchProfile matchProfile) {
        PupperProfile pupperProfile = new PupperProfile();
        pupperProfile.setMatchProfile(matchProfile);
        pupperProfile.setName("Milo pie");
        Breed breed = new Breed();
        breed.setName("Yorkshire Terrier");
        pupperProfile.setBreed(breed);
        pupperProfile.setSex(Gender.MALE);
        pupperProfile.setEnergyLevel(Energy.LOW);
        pupperProfile.setBirthdate(Date.from(Instant.now().minus(600L, ChronoUnit.DAYS)));
        pupperProfile.setLifeStage(PupperUtils.dobToLifeStage(pupperProfile.getBirthdate()));
        pupperProfile.setFixed(true);

        return pupperProfile;
    }
}
