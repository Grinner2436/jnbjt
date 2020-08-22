package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

import javax.persistence.*;

/**
 * 发生条件
 */
@Data
@Embeddable
public class EnhancementQualification {

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Enumerated(EnumType.STRING)
    private Job job;
}
