package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Energy;
import com.utahmsd.pupper.dto.pupper.LifeStage;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//Entity repesenting each individual pupper's separate info (

@Entity
@Table(schema="u0934995", name = "Pupper")
public class PupperProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pupper_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    private char sex; //Male or female

    @Column(name = "birth_date")
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_stage")
    private LifeStage lifeStage;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breedId;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy")
    private Energy energy;

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

//    public Long getBreedId() {
//        return breedId;
//    }
//
//    public void setBreedId(Long breedId) {
//        this.breedId = breedId;
//    }

    public Breed getBreedId() {
        return breedId;
    }

    public void setBreedId(Breed breedId) {
        this.breedId = breedId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
