package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Building;
import com.grinner.game.jnbjt.domain.enums.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    Activity findByDescription(String description);
    List<Activity> findAllByBuilding(Building building);
}
