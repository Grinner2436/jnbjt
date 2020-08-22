package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 增强方式
 */
@Data
@Embeddable
public class EnhancementOperation {

    //操作数数
    private Integer operand;

    //操作符
    @Enumerated(EnumType.STRING)
    private Operation operation;

    //作用项目
    @Enumerated(EnumType.STRING)
    private OperationTarget operationTarget;
}
