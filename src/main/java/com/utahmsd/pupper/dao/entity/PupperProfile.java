package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity repesenting a profile belonging to each dog.
 * Multiple pupper profiles can belong to a single MatchProfile.
 *
 * All pupper profiles are associated with a match profile and user profile.
 */

@Entity
@Table(name = "pupper_profile",
       indexes = {@Index(columnList = "breed_id_fk", name = "pupper_profile_ibfk_2"),
                 @Index(columnList = "match_profile_id_fk", name = "pupper_profile_ibfk_4")})
public class PupperProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    //REMOVED THIS FIELD SINCE A PUPPER PROFILE IS ALWAYS ASSOCIATED WITH A MATCH PROFILE, AND A MATCH PROFILE REFERENCES A USER PROFILE ID.
//    @ManyToOne(fetch = FetchType.LAZY) //A user can have multiple puppers, but a pupper can only belong to one user
//    @JoinColumn(name = "user_profile_id_fk") //foreign key that references 'id' column in UserProfile entity
//    @Valid
//    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY) //A match profile can have multiple puppers, but a pupper can only belong to one match profile
    @JoinColumn(name = "match_profile_id_fk")
    @Valid
    private MatchProfile matchProfile;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Max(1)
    @Column(name = "sex")
    private char sex; //M or F

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birthdate")
    @Past
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "lifestage")
    private LifeStage lifeStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy")
    private Energy energy;

    @ManyToOne
    @JoinColumn(name = "breed_id_fk")
    @Valid
    private Breed breed;

    @Column(name = "is_fixed")
    private boolean isFixed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
//
//    public UserProfile getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(UserProfile userProfile) {
//        this.userProfile = userProfile;
//    }

    public MatchProfile getMatchProfile() { return matchProfile; }

    public void setMatchProfile(MatchProfile matchProfile) { this.matchProfile = matchProfile; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public LifeStage getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(LifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

}
