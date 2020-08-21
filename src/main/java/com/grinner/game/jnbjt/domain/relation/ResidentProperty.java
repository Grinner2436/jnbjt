package com.grinner.game.jnbjt.domain.relation;

import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

/**
 * 实际居民
 */
@Data
@Entity
public class ResidentProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    @OneToOne
    @JoinColumn(name = "talent_stage_id")
    private TalentStage talentStage;

    @OneToOne
    @JoinColumn(name = "house_id")
    private House house;

    @OneToOne
    @JoinColumn(name = "resident_level")
    private ResidentLevel residentLevel;

    private Integer residentLevelStage;
    private Integer talentLevelStage;

    @ElementCollection
    private Map<Profession, Integer> attributeValues;

}
