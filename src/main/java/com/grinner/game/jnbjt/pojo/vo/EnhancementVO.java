package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import lombok.Data;

import java.util.List;

/**
 * 发挥优势
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EnhancementVO {

    private Integer id;

    private Integer talentStageId;

    protected EnhancementQualificationVO qualification;

    protected List<EnhancementOperationVO> operations;
}
