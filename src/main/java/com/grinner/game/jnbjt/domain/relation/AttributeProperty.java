package com.grinner.game.jnbjt.domain.relation;

import com.grinner.game.jnbjt.domain.enums.AttrbuiteLevel;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 实际居民属性
 */
@Data
@Embeddable
public class AttributeProperty {

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Enumerated(EnumType.STRING)
    private AttrbuiteLevel attrbuiteLevel;

    private Integer maxAttrbuiteVal;
}
