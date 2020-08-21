package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 发生条件
 */
@Data
@Entity
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "investment_id")
    private Investment investment;

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;
}
