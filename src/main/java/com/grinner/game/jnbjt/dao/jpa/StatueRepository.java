package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Statue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatueRepository extends JpaRepository<Statue,Integer> {
    Statue findByName(String name);
}
