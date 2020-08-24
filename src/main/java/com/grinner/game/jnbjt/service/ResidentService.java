package com.grinner.game.jnbjt.service;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.ResidentRepository;
import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.entity.Talent;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<JSONObject> getAllResidentTalentStage(){
        List<Resident> residents =  residentRepository.findAll();
        List<JSONObject> result = residents.stream().map(resident -> {
            Talent talent = resident.getTalent();
            List<TalentStage> stages = talent.getStages();
            if(stages != null && ! stages.isEmpty()){
                List<JSONObject> stageInfoList = stages.parallelStream().map(talentStage ->{
                    JSONObject stageInfo = new JSONObject();
                    stageInfo.put("resident", resident.getName());
                    stageInfo.put("talent", talent.getName());
                    stageInfo.put("stageId", talentStage.getId());
                    stageInfo.put("stageLevel", talentStage.getLevel());
                    stageInfo.put("stageDescription", talentStage.getDescription());
                    stageInfo.put("stageStatus", talentStage.getUpdated());
                    stageInfo.put("talentDetails", talentStage.getEnhancements());
                    return stageInfo;
                }).collect(Collectors.toList());
                return stageInfoList;
            }
            return null;
        }).filter(stageInfoList ->  stageInfoList != null).flatMap(Collection::parallelStream).collect(Collectors.toList());
        return result;
    }
}
