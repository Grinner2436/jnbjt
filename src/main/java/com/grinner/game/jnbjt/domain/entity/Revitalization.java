package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Operation;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 官府活动
 */
@Data
@ToString(exclude = {"activities"})
@Entity
public class Revitalization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ElementCollection
    private Map<Activity, Integer> activities;

    //操作数
    private Integer operand;

    //操作符
    @Enumerated(EnumType.STRING)
    private Operation operation;
}
