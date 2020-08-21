package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.BuildingProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 活动
 */
@Data
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //活动执行的优先级
    private Integer priority;
    //净收入（人分钟）
    private Integer income;
    private String description;
    private Boolean needResident;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @OneToOne
    @JoinColumn(name = "investment_id")
    private Investment investment;

    @OneToOne
    @JoinColumn(name = "profit_id")
    private Profit profit;
}
