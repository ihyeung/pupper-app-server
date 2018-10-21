package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "breed")
public class Breed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String breed;

    @Column(name = "alt_name")
    private String altName;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private Size size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
