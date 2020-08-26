package com.grinner.game.jnbjt.pojo.vo.composite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinner.game.jnbjt.pojo.vo.ResidentVO;
import com.grinner.game.jnbjt.pojo.vo.TalentVO;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResidentTalentVO {
    private ResidentVO resident;
    private TalentVO talent;
}
