package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 资产
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AssetVO {

    private Integer id;
    //每份资产对应的“人时”
    private Integer value;

    protected String name;
}
