package com.grinner.game.jnbjt.controller;

import com.grinner.game.jnbjt.pojo.ao.EnhancementAO;
import com.grinner.game.jnbjt.service.AssetService;
import com.grinner.game.jnbjt.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("talent")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @PostMapping("/stage/enhancement/list")
    public String getAssetNameList(Integer stageId, List<EnhancementAO> enhancements){
        talentService.saveTalentStageEnhancements(stageId, enhancements);
        return "";
    }
}
