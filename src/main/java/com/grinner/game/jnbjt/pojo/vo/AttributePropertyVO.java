package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.AttrbuiteLevel;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

/**
 * 实际居民属性
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttributePropertyVO {

    private Profession profession;

    private AttrbuiteLevel attrbuiteLevel;

    private Integer attributeVal;
}
