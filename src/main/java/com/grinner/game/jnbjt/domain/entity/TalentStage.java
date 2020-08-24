package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 天赋成长阶段
 */
@Data
@ToString(exclude = {"enhancements","talent"})
@Entity
public class TalentStage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "talent_id")
    private Talent talent;

    private Integer level;

    private String description;

    private Integer fragmentVolume;

    private Boolean updated;

    @OneToMany
    private List<Enhancement> enhancements;
}
