package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 发挥优势
 */
@Data
@Entity
public class Enhancement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //发生条件
    @OneToOne
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;

    //作用形式
    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

//    @OneToOne
//    @JoinColumn(name = "formula_id")
    private Formula formula;

    private Integer factor;
}
