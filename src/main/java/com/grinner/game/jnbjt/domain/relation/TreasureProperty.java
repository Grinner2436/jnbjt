package com.grinner.game.jnbjt.domain.relation;

import com.grinner.game.jnbjt.domain.entity.TalentStage;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.entity.Treasure;
import com.grinner.game.jnbjt.domain.entity.TreasureLevel;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

/**
 * 实际珍宝属性
 */
@Data
@Entity
public class TreasureProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "treasure_id")
    private Treasure treasure;

    @OneToOne
    @JoinColumn(name = "resident_property_id")
    private ResidentProperty residentProperty;

    @OneToOne
    @JoinColumn(name = "treasure_level")
    private TreasureLevel treasureLevel;

    private Integer treasureLevelStage;

    @ElementCollection
    private Map<Profession, Integer> attributeValues;


    @OneToOne
    @JoinColumn(name = "talent_stage")
    private TalentStage talentStage;
}
