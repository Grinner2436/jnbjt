package com.grinner.game.jnbjt.domain.instance;

import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Building;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 实际建筑
 */
@Data
@Entity
public class BuildingProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    private Integer level;

    @ElementCollection
    private List<Activity> activities;
}
