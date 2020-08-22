package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.BuildingType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 建筑
 */
@Data
@Entity
public class Building {

    public static Building ANY_BUILDING;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //-1无限制，0不能升级，其他为具体等级
    private Integer levelLimitation;

    //建造上限
    private String amountLimitation;

    //可解锁建造的等级
    private Integer unlockLevel;

    private String name;

    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;

    @ManyToMany
    private List<StateCapital> stateCapitals;
}
