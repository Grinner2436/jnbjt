package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

/**
 * 活动
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ActivityVO {

    private Integer id;

    private Integer priority;
    private Integer income;
    private String description;
    private Boolean needResident;

    private BuildingVO building;

    private Profession profession;

    private Job job;

    private InvestmentVO investment;

    private ProfitVO profit;
}
