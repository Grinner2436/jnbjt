package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 投资项内容
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AssetPropertyVO {

    private Integer assetId;

    private Integer amount;
}
