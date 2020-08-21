package com.grinner.game.jnbjt.domain.relation;

import com.grinner.game.jnbjt.domain.entity.People;
import com.grinner.game.jnbjt.domain.entity.StateCapital;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Map;

/**
 * 民居
 */
@Data
@Entity
public class House extends  BuildingProperty {

    @OneToOne
    @JoinColumn(name = "city_id")
    private StateCapital city;

    @OneToOne(mappedBy = "house")
    private ResidentProperty residentProperty;

    @ElementCollection
    private Map<Integer, People> household;
}
