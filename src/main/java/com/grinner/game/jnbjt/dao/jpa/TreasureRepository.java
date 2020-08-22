package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Treasure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreasureRepository extends JpaRepository<Treasure,Integer> {
    boolean existsByName(String name);
}
