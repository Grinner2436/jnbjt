package com.grinner.game.jnbjt.domain.instance;

import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.entity.ResidentLevel;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
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
    @JoinColumn(name = "house_id")
    private House house;

    //级别的阶
    @ManyToOne
    @JoinColumn(name = "resident_level")
    private ResidentLevel residentLevel;

    //级别的经验
    private Integer residentLevelStage;

    //天赋的阶
    @OneToOne
    @JoinColumn(name = "talent_stage_id")
    private TalentStage talentStage;

    //天赋的经验
    private Integer talentLevelStage;

    @ElementCollection
    private Map<Profession, AttributeProperty> attributeValues;

}
