package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 居民等级
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResidentLevel {

    private Integer id;

    private Integer level;
    private Integer experienceVolume;
}
