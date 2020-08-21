package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.StateCapital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<StateCapital,Integer> {
    StateCapital findByName(String name);
}
