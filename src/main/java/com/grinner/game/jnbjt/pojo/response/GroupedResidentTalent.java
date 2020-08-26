package com.grinner.game.jnbjt.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.pojo.vo.composite.ResidentTalentVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GroupedResidentTalent {
    private Map<String, List<ResidentTalentVO>> residentGroup;
}
