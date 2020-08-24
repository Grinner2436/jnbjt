package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.ResidentLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentLevelRepository extends JpaRepository<ResidentLevel,Integer> {
    ResidentLevel findByLevel(Integer level);
}
