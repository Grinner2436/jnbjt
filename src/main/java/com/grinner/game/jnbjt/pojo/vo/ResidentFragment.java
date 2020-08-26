package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 居民碎片
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResidentFragment {

    private Integer id;

    private ResidentVO resident;
}
