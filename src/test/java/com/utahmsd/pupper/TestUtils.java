package com.utahmsd.pupper;

import com.utahmsd.pupper.dao.entity.*;
import com.utahmsd.pupper.dto.pupper.*;
import com.utahmsd.pupper.util.ProfileUtils;
import com.utahmsd.pupper.util.Utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        user.setProfileImage("https://www.shutterstock.com/image-photo/beautiful-young-woman-clean-fresh-skin-519177016");

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
        matchProfile.setLifeStage(ProfileUtils.dobToLifeStage(matchProfile.getBirthdate()));
        matchProfile.setNumDogs(2);
        matchProfile.setSex(Gender.FEMALE);
        matchProfile.setZipRadius(3);
        matchProfile.setProfileImage("https://images.freeimages.com/images/large-previews/ae2/yorkie-3-1362057.jpg");
        matchProfile.setIsDefault(true);
        matchProfile.setIsHidden(false);
        matchProfile.setShowSimilar(false);
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
        pupperProfile.setLifeStage(ProfileUtils.dobToLifeStage(pupperProfile.getBirthdate()));
        pupperProfile.setFixed(true);

        return pupperProfile;
    }

    public static MatchResult createMatchResult(MatchProfile matchProfile, MatchProfile matchProfile1) {
        MatchResult result = new MatchResult();
        result.setMatchProfileOne(matchProfile);
        result.setMatchProfileTwo(matchProfile1);
        result.setMatchForProfileOne(true);
        result.setMatchForProfileTwo(true);
        result.setBatchSent(Instant.now().minus(6L, ChronoUnit.HOURS));
        result.setRecordExpires(Instant.now().plus(6L, ChronoUnit.HOURS));
        result.setResultCompleted(Instant.now());

        return result;
    }

    public static PupperMessage createMessage(MatchProfile matchProfile, MatchProfile matchProfile1) {
        PupperMessage message = new PupperMessage();

        message.setMatchProfileSender(matchProfile);
        message.setMatchProfileReceiver(matchProfile1);
        message.setTimestamp(Utils.getIsoFormatTimestamp(Instant.now()));
        message.setMessage("Test utils test message");
        return message;
    }

    public static List<MatchPreference> createMatchPreferences(MatchProfile matchProfile) {
        List<MatchPreference> matchPreferences = new ArrayList<>();
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.AGE, "ALL"));
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.ENERGY, Energy.MED.value()));
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.ENERGY, Energy.LOW.value()));
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.ENERGY, Energy.MIN.value()));
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.SIZE, Size.TOY.value()));
        matchPreferences.add(createMatchPreference(matchProfile, PreferenceType.SIZE, Size.SMALL.value()));

        return matchPreferences;
    }

    private static MatchPreference createMatchPreference(MatchProfile matchProfile, PreferenceType type, String preference) {
        MatchPreference matchPreference = new MatchPreference();
        matchPreference.setMatchProfile(matchProfile);
        matchPreference.setPreferenceType(type);
        matchPreference.setMatchingPreference(preference);

        return matchPreference;
    }
}
