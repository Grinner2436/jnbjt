package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 投入
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class InvestmentVO {

    private Integer id;

    private String name;

    private Integer activityId;

    private List<AssetPropertyVO> assetProperties;
}
