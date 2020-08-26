package com.grinner.game.jnbjt.service;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.ResidentRepository;
import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.entity.Talent;
import com.grinner.game.jnbjt.domain.entity.TalentStage;
import com.grinner.game.jnbjt.domain.enums.ResidentGrade;
import com.grinner.game.jnbjt.pojo.response.GroupedResidentTalent;
import com.grinner.game.jnbjt.pojo.vo.ResidentVO;
import com.grinner.game.jnbjt.pojo.vo.TalentVO;
import com.grinner.game.jnbjt.pojo.vo.composite.ResidentTalentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public GroupedResidentTalent getAllResident(){
        GroupedResidentTalent result = new GroupedResidentTalent();
        List<Resident> residents =  residentRepository.findAll();
        Map<ResidentGrade, List<Resident>> sourceGroup = residents.stream().collect(Collectors.groupingBy(resident -> resident.getGrade()));
        Map<String, List<ResidentTalentVO>> residentGroup = new HashMap<>();
        for(ResidentGrade grade : ResidentGrade.values()){
            List<Resident> residentList = sourceGroup.get(grade);
            if (residentList == null){
                continue;
            }
            List<ResidentTalentVO> residentTalentList = residentList.stream().map(resident -> {
                ResidentVO residentVO = new ResidentVO();
                residentVO.setId(resident.getId());
                residentVO.setName(resident.getName());
                residentVO.setAvatar(resident.getAvatar());

                Talent talent = resident.getTalent();
                TalentVO talentVO = new TalentVO();
                talentVO.setId(talent.getId());
                talentVO.setName(talent.getName());

                ResidentTalentVO residentTalentVO = new ResidentTalentVO();
                residentTalentVO.setResident(residentVO);
                residentTalentVO.setTalent(talentVO);
                return residentTalentVO;
            }).collect(Collectors.toList());
            residentGroup.put(grade.getDescription(), residentTalentList);
        }
        result.setResidentGroup(residentGroup);
        return result;
    }
}
