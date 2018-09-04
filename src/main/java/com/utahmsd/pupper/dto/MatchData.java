package com.utahmsd.pupper.dto;

import javax.persistence.*;

//Entity representing all pairings of puppers who have swiped left or right on each other

@Entity
@Table(name = "match")
public class MatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pupper_id_1")
    private Long pupId1;

    @Column(name = "pupper_id_2")
    private Long pupId2;

    @Column(name = "is_match")
    private boolean isMatch;

}
