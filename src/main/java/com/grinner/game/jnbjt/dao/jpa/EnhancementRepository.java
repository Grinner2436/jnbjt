package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Enhancement;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnhancementRepository extends JpaRepository<Enhancement,Integer> {
    int deleteByTalentStage(TalentStage talentStage);
}
