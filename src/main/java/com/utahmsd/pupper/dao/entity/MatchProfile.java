package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static com.utahmsd.pupper.util.Constants.DATE_FORMAT;
import static com.utahmsd.pupper.util.Constants.DATE_FORMATTER;

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

    @ManyToOne //A user can have multiple match profiles
    // Removed FetchType.LAZY to resolve HttpMessageNotWritableException: Could not write content: could not initialize proxy - no Session
    @JoinColumn(name = "user_profile_id_fk")
    @Valid
    private UserProfile userProfile;

    @Column(name = "names")
    @NotBlank
    private String names;

    @DefaultValue("1")
    @Column(name = "num_dogs")
    private int numDogs;

    @Column(name = "sex")
    @javax.validation.constraints.Size(min = 1, max = 1)
    private String sex; //M or F

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @Column(name = "birthdate")
    @Past
    private Date birthdate;

    @ManyToOne //Many match profiles can belong to a type of breed, but a match profile can only be one breed
    // Removed FetchType.LAZY to resolve HttpMessageNotWritableException: Could not write content: could not initialize proxy - no Session
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

    /**
     * Returns a list of field names that are valid param keys to filter matchProfiles by.
     * @return
     */
    public static List<String> matchProfileFieldList() {
        return Arrays.asList("id", "numDogs", "names", "sex", "birthdate", "breed", "size", "energyLevel", "lifeStage",
                "score", "aboutMe", "profileImage", "userProfile");
    }

    public static MatchProfile createFromObject(Object object) throws ParseException {
        if (object != null) {
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;
            MatchProfile matchProfile = new MatchProfile();
            matchProfile.setId((Long) entityObject.get("id"));
            matchProfile.setUserProfile(UserProfile.createFromObject(entityObject.get("userProfile")));
            matchProfile.setNames((String) entityObject.get("names"));
            matchProfile.setBirthdate(DATE_FORMATTER.parse((String) entityObject.get("birthdate")));
            matchProfile.setSex((String) entityObject.get("sex"));
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

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public UserProfile getUserProfile() { return userProfile; }

    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }

    public String getNames() { return names; }

    public void setNames(String names) { this.names = names; }

    public int getNumDogs() { return numDogs; }

    public void setNumDogs(int numDogs) { this.numDogs = numDogs; }

    public String getSex() { return sex; }

    public void setSex(String sex) { this.sex = sex; }

    public Date getBirthdate() { return birthdate; }

    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public Breed getBreed() { return breed; }

    public void setBreed(Breed breed) { this.breed = breed; }

    public Size getSize() { return size; }

    public void setSize(Size size) { this.size = size; }

    public Energy getEnergyLevel() { return energyLevel; }

    public void setEnergyLevel(Energy energyLevel) { this.energyLevel = energyLevel; }

    public LifeStage getLifeStage() { return lifeStage; }

    public void setLifeStage(LifeStage lifeStage) { this.lifeStage = lifeStage; }

    public float getScore() { return score; }

    public void setScore(float score) { this.score = score; }

    public String getAboutMe() { return aboutMe; }

    public void setAboutMe(String aboutMe) { this.aboutMe = aboutMe; }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
