package com.utahmsd.pupper.dto;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pupper_id_1")
    private Long pupId1;

    @Column(name = "pupper_id_2")
    private Long pupId2;

    @Column(name = "is_match")
    private boolean isMatch;

    @Column(name = "timestamp")
    private Timestamp timestamp;

}
