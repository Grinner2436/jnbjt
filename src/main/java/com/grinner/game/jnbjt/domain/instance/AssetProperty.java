package com.grinner.game.jnbjt.domain.instance;

import lombok.Data;

import javax.persistence.*;

/**
 * 投资项内容
 */
@Data
@Embeddable
public class AssetProperty {

    @JoinColumn(table = "asset",name = "asset_name",columnDefinition = "name")
    private String assetName;

    private Double amount;
}
