package com.grinner.game.jnbjt.dao.mybatis;

import com.grinner.game.jnbjt.domain.entity.Resident;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentDao {

    @SelectProvider(ResidentProvider.class)
    List<Resident> listResidents(DaoBooleanStatus owned, String orderField);
}
