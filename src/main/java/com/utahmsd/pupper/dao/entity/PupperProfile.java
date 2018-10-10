package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

//Entity repesenting each individual pupper's separate info (

@Entity
@Table(schema="u0934995", name = "pupper_profile")
public class PupperProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pupper_id", updatable = false, nullable = false)
    private Long id;

    @Size(min = 2, max = 30)
    @Column(name = "name")
    private String name;

    @Max(1)
    @Column(name = "sex")
    private char sex; //M or F

    @Column(name = "birth_date")
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_stage")
    private LifeStage lifeStage;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy")
    private Energy energy;

    @Valid
    @ManyToOne //A user can have multiple puppers, but a pupper can only belong to one user
    @JoinColumn(name = "user_profile_id") //foreign key that references 'id' column in UserProfile entity
    private UserProfile userProfile;

    @Valid
    @ManyToOne //A match profile can have multiple puppers, but a pupper can only belong to one match profile
    @JoinColumn(name = "match_profile_id")
    private MatchProfile matchProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Breed getBreedId() {
        return breed;
    }

    public void setBreedId(Breed breed) {
        this.breed = breed;
    }

    public boolean isNeutered() {
        return isNeutered;
    }

    public void setNeutered(boolean neutered) {
        isNeutered = neutered;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public MatchProfile getMatchProfile() { return matchProfile; }

    public void setMatchProfile(MatchProfile matchProfile) { this.matchProfile = matchProfile; }
}
