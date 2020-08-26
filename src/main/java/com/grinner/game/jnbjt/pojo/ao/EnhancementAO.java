package com.grinner.game.jnbjt.pojo.ao;

import com.grinner.game.jnbjt.domain.entity.EnhancementOperation;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import com.grinner.game.jnbjt.domain.enums.Profession;
import lombok.Data;

@Data
public class EnhancementAO {
    private Integer enhancementId;
    private Integer stageId;
    private Integer activityId;
    private Integer buildingId;
    private Integer assetId;
    private Integer operand;
    private Profession profession;
    private Job job;
    private OperationTarget operationTarget;
    private Operation operation;
}
