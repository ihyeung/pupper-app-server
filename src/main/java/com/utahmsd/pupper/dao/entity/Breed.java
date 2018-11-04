package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "name")
public class Breed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
