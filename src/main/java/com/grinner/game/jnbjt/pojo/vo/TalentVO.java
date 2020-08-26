package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 天赋
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TalentVO {

    private Integer id;

    private String name;

    private List<TalentStageVO> stages;

    private Profession profession;

    private Job job;

    private BuildingVO building;

    private String description;
}
