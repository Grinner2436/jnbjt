package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.enums.ResidentGrade;
import com.grinner.game.jnbjt.domain.relation.AttributeProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 居民
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResidentVO extends People {

    private String avatar;

    private ResidentGrade grade;

    private TalentVO talent;

    private List<AttributePropertyVO> minAttributes;

    private List<AttributePropertyVO> maxAttributes;

    private List<BookVO> preferredbooks;
}
