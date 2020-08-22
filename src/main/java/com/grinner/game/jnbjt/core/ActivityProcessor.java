package com.grinner.game.jnbjt.core;

import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.relation.ResidentProperty;
import com.grinner.game.jnbjt.domain.relation.TreasureProperty;

public interface ActivityProcessor {

    void compute(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty);
}
