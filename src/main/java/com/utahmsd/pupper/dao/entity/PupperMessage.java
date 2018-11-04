package com.utahmsd.pupper.dao.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pupper_message")
public class PupperMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_profile_id_fk_1")
    private MatchProfile matchProfileSender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_profile_id_fk_2")
    private MatchProfile matchProfileReceiver;

    private Date timestamp;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchProfile getMatchProfileSender() {
        return matchProfileSender;
    }

    public void setMatchProfileSender(MatchProfile matchProfileSender) {
        this.matchProfileSender = matchProfileSender;
    }

    public MatchProfile getMatchProfileReceiver() {
        return matchProfileReceiver;
    }

    public void setMatchProfileReceiver(MatchProfile matchProfileReceiver) {
        this.matchProfileReceiver = matchProfileReceiver;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
