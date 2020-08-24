package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Talent;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentStageRepository extends JpaRepository<TalentStage,Integer> {
    TalentStage findByTalentAndLevel(Talent talent, Integer integer);
}
