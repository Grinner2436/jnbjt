package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.BuildingType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 雕像1
 */
@Data
@Entity
//@PrimaryKeyJoinColumn
//        (referencedColumnName = "special_building")
public class Statue extends Building{

    private String effect;

    private Double effectValue;

    private String effectRange;

    @ManyToMany
    private List<Building> buildings;
}
