package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 州府
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StateCapitalVO {

    private Integer id;

    private String name;
}
