package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "breed")
public class Breed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "breed")
    private String breed;

    @Column(name = "size")
    private Size size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
