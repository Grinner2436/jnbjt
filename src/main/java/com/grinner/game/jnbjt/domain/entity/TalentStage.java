package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 天赋成长阶段
 */
@Data
@Entity
public class TalentStage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "talent_id")
    private Talent talent;

    private Integer level;

    private String description;

    private Integer fragmentVolume;

    @OneToOne
    @JoinColumn(name = "enhancement_id")
    private Enhancement enhancement;

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;
}
