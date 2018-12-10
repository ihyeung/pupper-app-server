package com.utahmsd.pupper.dao.entity;

import com.utahmsd.pupper.dto.pupper.Size;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedHashMap;

@Entity
@Table(name = "breed", indexes = {@Index(columnList = "name", name = "name")})
public class Breed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "alt_name")
    private String altName;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private Size size;

    public static Breed createFromObject(Object object) {
        if (object != null) {
            LinkedHashMap<Object, Object> entityObject = (LinkedHashMap<Object, Object>) object;
            Breed breed = new Breed();
            breed.setId((Long) entityObject.get("id"));
            breed.setName((String) entityObject.get("name"));
            breed.setAltName((String) entityObject.get("altName"));
            breed.setSize(Size.fromValue((String) entityObject.get("size")));
            return breed;
        }
        return null;
    }

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
