package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pup.Energy;
import com.utahmsd.pupper.dto.pup.LifeStage;

import javax.persistence.*;
import java.io.Serializable;

//Entity repesenting each individual pupper's separate info (

@Entity
@Table(name = "pupper")
public class PupperProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pupper_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_male")
    private boolean isMale; //Male or female

    @Column(name = "birth_month")
    private int month;

    @Column(name = "birth_year")
    private int year;

    @Column(name = "life_stage")
    private LifeStage lifeStage;

    @Column(name = "breed")
    private Breed breed;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    @Column(name = "energy")
    private Energy energy;

//    @ManyToOne //Multiple puppers can reference a single MatchProfile
//    @JoinColumn(name = "match_profile_id") //foreign key referencing 'id' column in MatchProfile entity
    @Column(name = "match_profile_id")
    private Long matchProfileId;

//    @ManyToOne //Many puppers can point to userid
//    @JoinColumn(name = "user_id") //foreign key that references 'id' column in UserProfile entity
    @Column(name = "user_id")
    private Long userId;

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

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public LifeStage getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(LifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
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

    public Long getMatchProfileId() {
        return matchProfileId;
    }

    public void setMatchProfileId(Long matchProfileId) {
        this.matchProfileId = matchProfileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
