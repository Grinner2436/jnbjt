package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 珍宝
 */
@Data
@Entity
public class Treasure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne
    @JoinColumn(name = "talent_id")
    private Talent talent;

    @OneToOne
    @JoinColumn(name = "talent_stage")
    private TalentStage talentStage;
}
