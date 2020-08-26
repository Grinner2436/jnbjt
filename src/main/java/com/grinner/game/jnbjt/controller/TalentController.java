package com.grinner.game.jnbjt.controller;

import com.alibaba.fastjson.JSONArray;
import com.grinner.game.jnbjt.pojo.ao.EnhancementAO;
import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.vo.TalentStageVO;
import com.grinner.game.jnbjt.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("talent")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @GetMapping("/stage/list")
    public List<TalentStageVO> getResidentTalentList(Integer talentId){
        List<TalentStageVO> data = talentService.getTalentStages(talentId);
        return data;
    }

    @PostMapping("/stage/enhancement/list")
    public OperationResult setResidentTalentList(Integer stageId, String enhancements){
        List<EnhancementAO> enhancements2= JSONArray.parseArray(enhancements,EnhancementAO.class);
        OperationResult operationResult = talentService.setResidentTalentList(stageId, enhancements2);
        return operationResult;
    }
}
