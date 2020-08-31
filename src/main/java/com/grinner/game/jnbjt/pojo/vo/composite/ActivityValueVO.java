package com.grinner.game.jnbjt.pojo.vo.composite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.pojo.vo.BuildingVO;
import com.grinner.game.jnbjt.pojo.vo.InvestmentVO;
import com.grinner.game.jnbjt.pojo.vo.ProfitVO;
import lombok.Data;

/**
 * 活动
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ActivityValueVO {

    private String actityName;
    private String residentName;
    private Double value;
    private ProfitVO profit;
}
