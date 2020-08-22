package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.relation.ResidentProperty;
import com.grinner.game.jnbjt.domain.relation.TreasureProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 发挥优势
 */
@Entity
public abstract class Enhancement {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "talent_stage_id")
    private TalentStage talentStage;

    //作用资源
    @OneToOne
    @JoinColumn(name = "asset_id")
    protected Asset asset;

    @Embedded
    protected EnhancementQualification qualification;

    @Embedded
    protected EnhancementOperation operation;

    public abstract void operate(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty);
}
