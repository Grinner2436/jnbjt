package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.enums.ResidentGrade;
import com.grinner.game.jnbjt.domain.relation.AttributeProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 居民
 */
@Data
@Entity
public class Resident extends People{

    @Enumerated(EnumType.STRING)
    private ResidentGrade grade;

    @OneToOne
    @JoinColumn(name = "talent_id")
    private Talent talent;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Profession, AttributeProperty> attributes;

    @ManyToMany
    private List<Book> preferredbooks;
}
