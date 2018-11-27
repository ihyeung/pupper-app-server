package com.utahmsd.pupper.dto.pupper;

import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;

import java.text.SimpleDateFormat;

import static com.utahmsd.pupper.util.PupperUtils.createAgeStringFromDate;

/**
 * POJO class representing all of the data that will be contained/displayed on a individual profile card.
 */
public class ProfileCard {

    private Long profileId;
    private String name;
    private String ageWithUnits;
    private char sex;
    private String breedName;
    private String location;
    private String aboutMe;
    private String lastActive;
    private String profileImage;
//    private String energyLevel;
//    private boolean isFixed;
//    private int numDogs;

    //Main implementation
    public static ProfileCard createFromMatchProfile(MatchProfile matchProfile) {
        ProfileCard card = new ProfileCard();
        card.setProfileId(matchProfile.getId());
        card.setName(matchProfile.getNames());
        card.setSex(matchProfile.getSex());
        card.setAgeWithUnits(createAgeStringFromDate(matchProfile.getBirthdate()));
        card.setBreedName(matchProfile.getBreed().getName());
        card.setLocation("SOUTH JORDAN, UTAH");
        card.setAboutMe(matchProfile.getAboutMe());
        card.setLastActive(new SimpleDateFormat("yyyy-MM-dd").format(matchProfile.getUserProfile().getLastLogin()));
        card.setProfileImage(matchProfile.getProfileImage());
//        card.setNumDogs(matchProfile.getNumDogs());

        return card;
    }

    //Potential simplified implementation for mvp
    public static ProfileCard createFromPupperProfile(PupperProfile pupperProfile) {
        ProfileCard card = new ProfileCard();
//        card.setNumDogs(1);
        card.setProfileId(pupperProfile.getId());
        card.setName(pupperProfile.getName());
        card.setSex(pupperProfile.getSex());
        card.setAgeWithUnits(createAgeStringFromDate(pupperProfile.getBirthdate()));
        card.setBreedName(pupperProfile.getBreed().getName());
        card.setLocation("SOUTH JORDAN, UTAH");
        card.setAboutMe(card.getAgeWithUnits() + " " + pupperProfile.getSex() + " " + pupperProfile.getBreed() +
                " with " + pupperProfile.getEnergy().value() + " energy, i am fixed: " + Boolean.toString(pupperProfile.isFixed()));
        card.setLastActive(new SimpleDateFormat("yyyy-MM-dd").format(pupperProfile.getMatchProfile()
                .getUserProfile().getLastLogin()));
        card.setProfileImage(pupperProfile.getMatchProfile().getProfileImage());

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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    //    public String getEnergyLevel() {
//        return energyLevel;
//    }
//
//    public void setEnergyLevel(String energyLevel) {
//        this.energyLevel = energyLevel;
//    }
//
//    public boolean isFixed() {
//        return isFixed;
//    }
//
//    public void setFixed(boolean fixed) {
//        isFixed = fixed;
//    }

//    public int getNumDogs() {
//        return numDogs;
//    }
//
//    public void setNumDogs(int numDogs) {
//        this.numDogs = numDogs;
//    }
}
