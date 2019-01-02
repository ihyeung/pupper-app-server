package com.utahmsd.pupper.dto.pupper;

import com.amazonaws.util.StringUtils;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;

import java.util.ArrayList;
import java.util.List;

import static com.utahmsd.pupper.util.Constants.DEFAULT_ABOUT_ME;
import static com.utahmsd.pupper.util.ProfileUtils.createAgeStringFromDate;
import static com.utahmsd.pupper.util.ProfileUtils.lastActivityFromLastLogin;

/**
 * POJO class representing all of the data that will be contained/displayed on a individual profile card.
 */
public class ProfileCard {

    private Long profileId;
    private String name;
    private String ageWithUnits;
    private String sex;
    private String breedName;
    private String distance;
    private String aboutMe;
    private String lastActive;
    private String profileImage;
    private String energyLevel;
//    private boolean isFixed;
    private int numDogs;
    private Long userId;
    private Boolean isMatch; //Optionally set field for a profile displayed to the active user that has already rated the current user

    public static ArrayList<ProfileCard> matchProfileToProfileCardMapper(List<MatchProfile> matchProfileList) {
        ArrayList<ProfileCard> profileCards = new ArrayList<>();
        matchProfileList.forEach(matchProfile -> profileCards.add(createFromMatchProfile(matchProfile)));
        return profileCards;
    }

    public static ProfileCard createFromMatchProfile(MatchProfile matchProfile) {
        ProfileCard card = new ProfileCard();
        card.setProfileId(matchProfile.getId());
        card.setName(matchProfile.getNames());
        card.setSex(matchProfile.getSex().value());
        card.setAgeWithUnits(createAgeStringFromDate(matchProfile.getBirthdate()));
        card.setBreedName(matchProfile.getBreed().getName());
        card.setDistance(matchProfile.getUserProfile().getZip());
        card.setAboutMe(StringUtils.isNullOrEmpty(matchProfile.getAboutMe()) ? DEFAULT_ABOUT_ME : matchProfile.getAboutMe());
        card.setLastActive(lastActivityFromLastLogin(matchProfile.getUserProfile().getLastLogin()));
        card.setProfileImage(matchProfile.getProfileImage());
        card.setEnergyLevel(matchProfile.getEnergyLevel().value());
        card.setNumDogs(matchProfile.getNumDogs());
        card.setUserId(matchProfile.getUserProfile().getId());

        return card;
    }

    public static ProfileCard createFromPupperProfile(PupperProfile pupperProfile) {
        ProfileCard card = new ProfileCard();
        card.setProfileId(pupperProfile.getMatchProfile().getId());
        card.setName(pupperProfile.getName());
        card.setSex(pupperProfile.getSex().value());
        card.setAgeWithUnits(createAgeStringFromDate(pupperProfile.getBirthdate()));
        card.setBreedName(pupperProfile.getBreed().getName());
        card.setAboutMe(DEFAULT_ABOUT_ME);
        card.setLastActive(lastActivityFromLastLogin(pupperProfile.getMatchProfile().getUserProfile().getLastLogin()));
        card.setProfileImage(pupperProfile.getMatchProfile().getProfileImage());
        card.setEnergyLevel(pupperProfile.getEnergyLevel().value());
        card.setNumDogs(1);
        card.setUserId(pupperProfile.getMatchProfile().getUserProfile().getId());

        return card;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeWithUnits() {
        return ageWithUnits;
    }

    public void setAgeWithUnits(String ageWithUnits) {
        this.ageWithUnits = ageWithUnits;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAboutMe() { return aboutMe; }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEnergyLevel() { return energyLevel; }

    public void setEnergyLevel(String energyLevel) { this.energyLevel = energyLevel; }

//    public boolean isFixed() { return isFixed; }
//
//    public void setFixed(boolean fixed) { isFixed = fixed; }
//
    public int getNumDogs() { return numDogs; }

    public void setNumDogs(int numDogs) { this.numDogs = numDogs; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getMatch() { return isMatch; }

    public void setMatch(Boolean match) { isMatch = match; }
}
