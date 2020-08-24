package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.relation.ResidentProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentPropertyRepository extends JpaRepository<ResidentProperty,Integer> {
    ResidentProperty findByResident(Resident resident);
}
