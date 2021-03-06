package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utahmsd.pupper.dto.PupperEntity;
import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.Gender;
import com.utahmsd.pupper.dto.pupper.LifeStage;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

import static com.utahmsd.pupper.util.Constants.DATE_FORMAT;

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
public class PupperProfile extends PupperEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A match profile can have multiple puppers, but a pupper can only belong to one match profile
    @JoinColumn(name = "match_profile_id_fk")
    @Valid
    private MatchProfile matchProfile;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Gender sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @Column(name = "birthdate")
    @Past
    private Date birthdate;

    //TODO: change these field names to be consistent with matchProfile's field names
    @Enumerated(EnumType.STRING)
    @Column(name = "lifestage")
    private LifeStage lifeStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "energyLevel")
    private Energy energyLevel;

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

    public MatchProfile getMatchProfile() { return matchProfile; }

    public void setMatchProfile(MatchProfile matchProfile) { this.matchProfile = matchProfile; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
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

    public Energy getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Energy energyLevel) {
        this.energyLevel = energyLevel;
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
