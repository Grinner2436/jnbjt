package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 天赋
 */
@Data
@Entity
public class Talent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "talent")
    private List<TalentStage> stages;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Enumerated(EnumType.STRING)
    private Job job;

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    private String description;
}
