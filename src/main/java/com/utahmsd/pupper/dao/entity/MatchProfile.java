package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.text.ParseException;
import java.util.LinkedHashMap;

/**
 * Entity representing each individual MatchProfile associated with a user (for 1-3 PupperProfiles).
 * In order for a user to use the PupperMatcherService, they must have at least one MatchProfile.
 * If a user has multiple MatchProfiles, they will be required to select their active MatchProfile in order
 * to begin being shown other MatchProfiles.
 *
 * Note that multiple dogs can be added to a shared MatchProfile only if they are the same breed/size,
 * energy level, and lifestage.
 *
 *  * All match profiles are associated with a user profile.
 */

@Entity
@Table(name = "match_profile",
        indexes = {@Index(columnList = "breed_id_fk", name = "breed_id_fk"),
                @Index(columnList = "user_profile_id_fk", name = "user_id_fk")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MatchProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //A user can have multiple match profiles
    @JoinColumn(name = "user_profile_id_fk")
    @Valid
    private UserProfile userProfile;

    @DefaultValue("1")
    @Column(name = "num_dogs")
    private int numDogs;

    @ManyToOne //Many match profiles can belong to a type of breed, but a match profile can only be one breed
    @JoinColumn(name = "breed_id_fk")
    @Valid
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

    @Column(name = "score")
    @DefaultValue("100")
    private float score;

    @NotBlank
    @javax.validation.constraints.Size(max = 130)
    @Column(name = "about")
    private String aboutMe;

    @Column(name = "profile_image")
    @javax.validation.constraints.Size(max = 100)
    private String profileImage;

    public static MatchProfile createFromObject(Object object) throws ParseException {
        if (object != null) {
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;
            MatchProfile matchProfile = new MatchProfile();
            matchProfile.setId((Long) entityObject.get("id"));
            matchProfile.setUserProfile(
                    UserProfile.createFromObject(entityObject.get("userProfile")));
            matchProfile.setNumDogs(((Long) entityObject.get("numDogs")).intValue());
            matchProfile.setBreed(Breed.createFromObject(entityObject.get("breed")));
            matchProfile.setSize(Size.valueOf((String) entityObject.get("size")));
            matchProfile.setEnergyLevel(Energy.valueOf((String) entityObject.get("energyLevel")));
            matchProfile.setLifeStage(LifeStage.valueOf((String) entityObject.get("lifeStage")));
            matchProfile.setScore(((Double) entityObject.get("score")).floatValue());
            matchProfile.setAboutMe((String) entityObject.get("aboutMe"));
            matchProfile.setProfileImage((String) entityObject.get("profileImage"));

            return matchProfile;
        }
        return null;
    }

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

    public Size getSize() { return size; }

    public void setSize(Size size) { this.size = size; }

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
