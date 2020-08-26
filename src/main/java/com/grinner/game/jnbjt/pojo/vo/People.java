package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 居民
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class People {

    private Integer id;

    private String name;
}
