package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.RevitalizationRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Revitalization;
import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.response.OperationType;
import com.grinner.game.jnbjt.pojo.response.ResultType;
import com.grinner.game.jnbjt.pojo.vo.ActivityVO;
import com.grinner.game.jnbjt.pojo.vo.RevitalizationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RevitalizationService {

    @Autowired
    private RevitalizationRepository revitalizationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public OperationResult delRevitalizationDetail(Integer id){
        revitalizationRepository.deleteById(id);
        return new OperationResult();
    }

    public RevitalizationVO getRevitalizationDetail(Integer id){
        Optional<Revitalization> revitalizationOptional =  revitalizationRepository.findById(id);
        if(revitalizationOptional.isPresent()){
            Revitalization revitalization = revitalizationOptional.get();
            RevitalizationVO revitalizationVO = new RevitalizationVO();
            revitalizationVO.setId(revitalization.getId());
            revitalizationVO.setName(revitalization.getName());
            revitalizationVO.setOperation(revitalization.getOperation());
            revitalizationVO.setOperand(revitalization.getOperand());

            Map<Activity, Integer> activities = revitalization.getActivities();
            List<ActivityVO> activityList = activities.keySet().stream().map(activity -> {
                ActivityVO activityVO = new ActivityVO();
                activityVO.setId(activity.getId());
                activityVO.setDescription(activity.getDescription());
                activityVO.setIncome(activities.get(activity));
                return activityVO;
            }).collect(Collectors.toList());
            revitalizationVO.setActivities(activityList);
            return revitalizationVO;
        }
        return null;
    }
    public List<RevitalizationVO> getRevitalizationList(){
        List<Revitalization> residents =  revitalizationRepository.findAll();
        List<RevitalizationVO> revitalizationList = residents.stream().map(revitalization -> {
            RevitalizationVO revitalizationVO = new RevitalizationVO();
            revitalizationVO.setId(revitalization.getId());
            revitalizationVO.setName(revitalization.getName());
            return revitalizationVO;
        }).collect(Collectors.toList());
        return revitalizationList;
    }

    public OperationResult setRevitalization(RevitalizationVO revitalizationAO) {
        OperationResult operationResult = new OperationResult();
        operationResult.setResultType(ResultType.SUCCESS);
        List<ActivityVO> activityList = revitalizationAO.getActivities();
        if(activityList == null || activityList.isEmpty()){
            operationResult.setResultType(ResultType.FAILED);
            operationResult.setReason("没有关联的活动");
            return operationResult;
        }
        Map<Activity, Integer> activities = activityList.stream().collect(Collectors.toMap(
                activityVO -> activityRepository.getOne(activityVO.getId()),
                activityVO -> activityVO.getIncome(),
                (v1, v2) -> v1));
        Integer id = revitalizationAO.getId();
        Revitalization revitalization = null;
        if(id != null){
            revitalization = revitalizationRepository.findById(id).get();
            operationResult.setOperationType(OperationType.Update);
        }else {
            revitalization = new Revitalization();
            operationResult.setOperationType(OperationType.Save);
        }
        revitalization.setName(revitalizationAO.getName());
        revitalization.setOperation(revitalizationAO.getOperation());
        revitalization.setOperand(revitalizationAO.getOperand());

        revitalization.setActivities(activities);
        revitalizationRepository.save(revitalization);
        return operationResult;
    }
}
