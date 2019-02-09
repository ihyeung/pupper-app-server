package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utahmsd.pupper.dto.pupper.PreferenceType;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;

@Entity
@Table(name = "match_preference",
        indexes = @Index(columnList = "match_profile_id_fk", name = "match_preference_ibfk_1"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MatchPreference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne //A match profile can have many preferences, and a match preference is associated with one match profile
    @JoinColumn(name = "match_profile_id_fk")
    @Valid
    private MatchProfile matchProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type")
    private PreferenceType preferenceType;

    @Column(name = "preference")
    private String matchingPreference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchProfile getMatchProfile() {
        return matchProfile;
    }

    public void setMatchProfile(MatchProfile matchProfile) {
        this.matchProfile = matchProfile;
    }

    public PreferenceType getPreferenceType() { return preferenceType; }

    public void setPreferenceType(PreferenceType preferenceType) { this.preferenceType = preferenceType; }

    public String getMatchingPreference() { return matchingPreference; }

    public void setMatchingPreference(String matchingPreference) {
        this.matchingPreference = matchingPreference;
    }
}
