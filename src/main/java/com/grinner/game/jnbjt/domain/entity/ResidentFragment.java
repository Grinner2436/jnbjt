package com.grinner.game.jnbjt.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 居民碎片
 */
@Data
@Entity
public class ResidentFragment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;
}
