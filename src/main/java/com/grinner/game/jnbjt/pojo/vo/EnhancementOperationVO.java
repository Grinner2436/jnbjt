package com.grinner.game.jnbjt.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import lombok.Data;

/**
 * 增强方式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EnhancementOperationVO {

    protected OperationTarget operationTarget;

    protected Integer assetId;

    //操作数数
    private Integer operand;

    //操作符
    private Operation operation;
}
