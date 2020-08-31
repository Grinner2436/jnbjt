package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.instance.ResidentProperty;
import com.grinner.game.jnbjt.domain.instance.TreasureProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 发挥优势
 */
@Data
@ToString(exclude = {"talentStage", "asset"})
@Entity
public abstract class Enhancement {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "talent_stage_id")
    private TalentStage talentStage;

    @Embedded
    protected EnhancementQualification qualification;

    @ElementCollection
    protected List<EnhancementOperation> operations;

    public abstract void operate(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty);
    public abstract void operate(Activity activity, Resident resident);
}
