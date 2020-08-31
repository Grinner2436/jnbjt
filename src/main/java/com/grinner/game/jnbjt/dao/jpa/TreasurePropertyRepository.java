package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Treasure;
import com.grinner.game.jnbjt.domain.instance.TreasureProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreasurePropertyRepository extends JpaRepository<TreasureProperty,Integer> {
    TreasureProperty findByTreasure(Treasure treasure);
}
