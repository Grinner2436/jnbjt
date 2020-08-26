package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

/**
 * 发生条件
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EnhancementQualificationVO {

    private Integer activityId;

    private Integer buildingId;

    private Profession profession;

    private Job job;
}
