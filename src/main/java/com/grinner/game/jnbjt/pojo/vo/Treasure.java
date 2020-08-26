package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 珍宝
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Treasure {

    private Integer id;

    private String name;

    private TalentVO talent;
}
