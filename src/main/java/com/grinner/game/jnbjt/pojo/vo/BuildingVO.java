package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.BuildingType;
import lombok.Data;

import java.util.List;

/**
 * 建筑
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BuildingVO {

    private Integer id;

    //-1无限制，0不能升级，其他为具体等级
    private Integer levelLimitation;

    //建造上限
    private String amountLimitation;

    //可解锁建造的等级
    private Integer unlockLevel;

    private String name;

    private BuildingType buildingType;

    private List<StateCapitalVO> stateCapitals;
}
