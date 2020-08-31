package com.grinner.game.jnbjt.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.grinner.game.jnbjt.config.MapJsonToAsset;
import com.grinner.game.jnbjt.domain.instance.AssetProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Map;

/**
 * 产出
 */
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"activity"})
@JsonIgnoreProperties({"activity"})
@Entity
public class Profit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne(mappedBy = "profit")
    private Activity activity;

    @ElementCollection
    @JsonDeserialize(keyUsing = MapJsonToAsset.class, keyAs = Asset.class, builder = Asset.class)
    private Map<Asset, AssetProperty> assetProperties;
}
