package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.relation.AssetProperty;

import javax.persistence.Embeddable;

@Embeddable
public abstract class Formula<T> {
    //TODO如何为每种计算提供公式
    public abstract AssetProperty calculate(AssetProperty input);
}
