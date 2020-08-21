package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 居民等级
 */
@Data
@Entity
public class ResidentLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer level;
    private Integer experienceVolume;
}
