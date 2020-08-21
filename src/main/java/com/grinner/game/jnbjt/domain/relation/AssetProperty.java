package com.grinner.game.jnbjt.domain.relation;

import com.grinner.game.jnbjt.domain.entity.Asset;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * 投资项内容
 */
@Data
@Embeddable
public class AssetProperty {

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @JoinColumn(table = "asset",name = "asset_name",columnDefinition = "name")
    private String assetName;

    private Integer amount;
}
