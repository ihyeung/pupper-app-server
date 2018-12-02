package com.utahmsd.pupper.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.utahmsd.pupper.util.Constants.ISO_DATE_FORMAT;

@Entity
@Table(name = "pupper_message",
        indexes = {@Index(columnList = "from_match_profile_id_fk", name = "pupper_message_ibfk_1"),
        @Index(columnList = "to_match_profile_id_fk", name = "pupper_message_ibfk_2")})
public class PupperMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_match_profile_id_fk")
    @Valid
    private MatchProfile matchProfileSender;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_match_profile_id_fk")
    @Valid
    private MatchProfile matchProfileReceiver;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss a") //eg. '2018-12-01 11:42:05 PM'
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz") //Standard ISO date format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_FORMAT) //Standard ISO date format
    private String timestamp;

    @Size(max = 500)
    @NotBlank
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
