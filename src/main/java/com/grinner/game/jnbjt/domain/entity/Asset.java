package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 资产
 */
@Data
@Entity
public class Asset {
    public static final String WORKER = "工人";

    public static Asset TIME;
    public static Asset ANYTHING;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //每份资产对应的“人时”
    private Double value;

    protected String name;
}
