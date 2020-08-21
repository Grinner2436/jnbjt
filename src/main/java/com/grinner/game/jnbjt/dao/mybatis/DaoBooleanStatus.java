package com.grinner.game.jnbjt.dao.mybatis;

public enum DaoBooleanStatus {
    ALL(-1),
    TRUE(1),
    FALSE(0),
    ;

    private int value;

    DaoBooleanStatus(int value){
        this.value = value;
    }
}
