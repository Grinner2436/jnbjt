package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 投入
 */
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"activity"})
@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne(mappedBy = "investment")
    private Activity activity;

    @ElementCollection
    private Map<Asset,AssetProperty> assetProperties;
}
