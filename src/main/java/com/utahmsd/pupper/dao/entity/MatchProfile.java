package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity representing each individual MatchProfile associated with a user (for 1-3 PupperProfiles).
 * In order for a user to use the PupperMatcherService, they must have at least one MatchProfile.
 * If a user has multiple MatchProfiles, they will be required to select their active MatchProfile in order
 * to begin being shown other MatchProfiles.
 *
 * Note that multiple dogs can be added to a shared MatchProfile only if they are the same breed/size,
 * energy level, and lifestage.
 */

@Entity
@Table(name = "match_profile")
public class MatchProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A user can have multiple match profiles
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @Column(name = "num_dogs")
    private int numDogs;

    @ManyToOne //Many match profiles can be of a certain breed, but a match profile can only be one breed
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy")
    private Energy energyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_stage")
    private LifeStage lifeStage;

    @Column(name = "pupper_score")
    private float score;

    @Column(name = "about")
    private String aboutMe;

    @Column(name = "profile_image")
    private String profileImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public int getNumDogs() {
        return numDogs;
    }

    public void setNumDogs(int numDogs) {
        this.numDogs = numDogs;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Energy getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Energy energyLevel) {
        this.energyLevel = energyLevel;
    }

    public LifeStage getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(LifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
