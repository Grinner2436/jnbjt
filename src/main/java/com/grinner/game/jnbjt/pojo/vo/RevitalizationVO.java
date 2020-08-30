package com.grinner.game.jnbjt.pojo.vo;

import com.grinner.game.jnbjt.domain.enums.Operation;
import lombok.Data;

import java.util.List;

/**
 * 官府活动
 */
@Data
public class RevitalizationVO {

    private Integer id;

    private String name;

    private String activityList;

    private List<ActivityVO> activities;

    //操作数
    private Integer operand;

    //操作符
    private Operation operation;
}
