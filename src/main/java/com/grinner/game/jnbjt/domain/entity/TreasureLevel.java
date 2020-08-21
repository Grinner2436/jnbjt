package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 珍宝等级
 */
@Data
@Entity
public class TreasureLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer level;
    private Integer breakLevel;
    private Boolean bottleneck;
    private Integer experienceVolume;
}
