package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.TreasureLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreasureLevelRepository extends JpaRepository<TreasureLevel,Integer> {
    TreasureLevel findByLevel(Integer level);
}
