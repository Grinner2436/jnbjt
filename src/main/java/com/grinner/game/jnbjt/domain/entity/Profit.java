package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 产出
 */
@Data
@ToString(exclude = {"activity"})
@Entity
public class Profit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "profit")
    private Activity activity;

    @ElementCollection
    private List<AssetProperty> assetProperties;
}
