package com.grinner.game.jnbjt.dao.mybatis;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

public class ResidentProvider implements ProviderMethodResolver {
    public String listResidents(DaoBooleanStatus owned, String orderField) {
        SQL sql = new SQL().SELECT("*").FROM("resident");
        if(owned != null && !DaoBooleanStatus.ALL.equals(owned)){
            sql.WHERE("is_owned = #{owned.value}");
        }
        return sql.toString();
    }
}
