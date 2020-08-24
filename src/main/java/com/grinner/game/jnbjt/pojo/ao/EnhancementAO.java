package com.grinner.game.jnbjt.pojo.ao;

import com.grinner.game.jnbjt.domain.entity.EnhancementOperation;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import lombok.Data;

@Data
public class EnhancementAO {
    private String assetName;
    private EnhancementOperation enhancementOperation;
//    private Operation operation;
//    private Integer operand;
//    private OperationTarget operationTarget;
}
