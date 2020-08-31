package com.grinner.game.jnbjt.core;

import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.instance.ResidentProperty;
import com.grinner.game.jnbjt.domain.instance.TreasureProperty;

public interface ActivityProcessor {

    void compute(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty);
}
