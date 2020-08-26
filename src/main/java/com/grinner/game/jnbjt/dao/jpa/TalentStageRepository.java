package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Talent;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentStageRepository extends JpaRepository<TalentStage,Integer> {
    TalentStage findByTalentAndLevel(Talent talent, Integer integer);
    List<TalentStage> findAllByTalentId(Integer talentId);
}
