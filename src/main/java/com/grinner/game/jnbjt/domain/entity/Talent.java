package com.grinner.game.jnbjt.domain.entity;

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

    @OneToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    @OneToMany(mappedBy = "talent")
    private List<TalentStage> stages;

    private String name;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private String description;
}
