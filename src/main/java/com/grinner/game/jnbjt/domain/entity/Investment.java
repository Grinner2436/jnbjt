package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 投入
 */
@Data
@ToString(exclude = {"activity"})
@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "investment")
    private Activity activity;

    @ElementCollection
    private List<AssetProperty> assetProperties;
}
