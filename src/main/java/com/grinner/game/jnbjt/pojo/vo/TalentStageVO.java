package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 天赋成长阶段
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TalentStageVO {

    private Integer id;

    private Integer talentId;

    private Integer level;

    private String description;

    private Integer fragmentVolume;

    private Boolean updated;

    private List<EnhancementVO> enhancements;
}
