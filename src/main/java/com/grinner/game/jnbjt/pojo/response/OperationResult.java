package com.grinner.game.jnbjt.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OperationResult {
    private Integer id;
    private Integer count;
    private String reason;
    private OperationType operationType;
    private ResultType resultType;
}
