package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.pojo.ao.EnhancementAO;
import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.response.OperationType;
import com.grinner.game.jnbjt.pojo.response.ResultType;
import com.grinner.game.jnbjt.pojo.vo.EnhancementOperationVO;
import com.grinner.game.jnbjt.pojo.vo.EnhancementQualificationVO;
import com.grinner.game.jnbjt.pojo.vo.EnhancementVO;
import com.grinner.game.jnbjt.pojo.vo.TalentStageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TalentService {

    @Autowired
    private TalentStageRepository talentStageRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EnhancementRepository enhancementRepository;

    public List<TalentStageVO> getTalentStages(Integer talentId){
        List<TalentStage> talentStages = talentStageRepository.findAllByTalentId(talentId);
        List<TalentStageVO> result = talentStages.stream().map(talentStage -> {
            TalentStageVO talentStageVO = new TalentStageVO();
            talentStageVO.setId(talentStage.getId());
            talentStageVO.setDescription(talentStage.getDescription());
            talentStageVO.setLevel(talentStage.getLevel());
            talentStageVO.setUpdated(talentStage.getUpdated());
            talentStageVO.setTalentId(talentId);

            List<EnhancementVO> enhancements = talentStage.getEnhancements().stream().map(enhancement -> {
                EnhancementVO enhancementVO = new EnhancementVO();
                enhancementVO.setId(enhancement.getId());
                enhancementVO.setOperationTarget(enhancement.getOperationTarget());
                enhancementVO.setTalentStageId(talentStage.getId());
                enhancementVO.setAssetId(enhancement.getAsset().getId());

                EnhancementQualificationVO qualificationVO = new EnhancementQualificationVO();
                qualificationVO.setActivityId(enhancement.getQualification().getActivity().getId());
                qualificationVO.setBuildingId(enhancement.getQualification().getBuilding().getId());
                qualificationVO.setProfession(enhancement.getQualification().getProfession());
                qualificationVO.setJob(enhancement.getQualification().getJob());
                enhancementVO.setQualification(qualificationVO);

                EnhancementOperationVO operationVO = new EnhancementOperationVO();
                operationVO.setOperand(enhancement.getOperation().getOperand());
                operationVO.setOperation(enhancement.getOperation().getOperation());
                enhancementVO.setOperation(operationVO);
                return enhancementVO;
            }).collect(Collectors.toList());
            talentStageVO.setEnhancements(enhancements);
            return talentStageVO;
        }).collect(Collectors.toList());
        return result;
    }

    @Transactional
    public OperationResult setResidentTalentList(Integer stageId, List<EnhancementAO> enhancementList){
        OperationResult operationResult = new OperationResult();
        Optional<TalentStage> stageOptional = talentStageRepository.findById(stageId);
        if(!stageOptional.isPresent()){
            operationResult.setResultType(ResultType.FAILED);
            operationResult.setReason("Stage 不存在");
            return operationResult;
        }
        TalentStage stage = stageOptional.get();
        Integer rows = enhancementRepository.deleteByTalentStage(stage);
        if (enhancementList.isEmpty()){
            operationResult.setResultType(ResultType.SUCCESS);
            operationResult.setOperationType(OperationType.Delete);
            operationResult.setCount(rows);
            return operationResult;
        }
        AtomicInteger count = new AtomicInteger();
        List<Enhancement> enhancements = enhancementList.stream().map(enhancementAO -> {
            Integer activityId = enhancementAO.getActivityId();
            Integer buildingId = enhancementAO.getBuildingId();
            Profession profession = enhancementAO.getProfession();
            Job job = enhancementAO.getJob();
            if(activityId == null && buildingId == null && profession == null && job == null){
                return null;
            }
            EnhancementQualification qualification = new EnhancementQualification();
            if(activityId != null && activityRepository.existsById(activityId)){
                Activity activity = activityRepository.getOne(activityId);
                qualification.setActivity(activity);
            }
            if(buildingId != null && buildingRepository.existsById(buildingId)){
                Building building = buildingRepository.getOne(buildingId);
                qualification.setBuilding(building);
            }
            qualification.setProfession(profession);
            qualification.setJob(job);

            Integer assetId = enhancementAO.getAssetId();
            OperationTarget operationTarget = enhancementAO.getOperationTarget();
            Operation operation = enhancementAO.getOperation();
            Integer operand = enhancementAO.getOperand();
            if(assetId == null || !assetRepository.existsById(assetId) || operationTarget == null || operation == null || operand == null){
                return null;
            }

            Integer enhancementId = enhancementAO.getEnhancementId();
            Enhancement enhancement = null;
            if(enhancementId != null && enhancementRepository.existsById(enhancementId)){
                enhancement = enhancementRepository.getOne(enhancementId);
            }else {
                enhancement = new GenericEnhancement();
            }

            enhancement.setOperationTarget(operationTarget);
            enhancement.setQualification(qualification);
            Asset asset = assetRepository.getOne(assetId);
            enhancement.setAsset(asset);
            EnhancementOperation enhancementOperation = new EnhancementOperation();
            enhancementOperation.setOperand(enhancementAO.getOperand());
            enhancementOperation.setOperation(enhancementAO.getOperation());
            enhancement.setOperation(enhancementOperation);

            enhancement.setTalentStage(stage);
            enhancementRepository.save(enhancement);
            count.incrementAndGet();
            return enhancement;
        }).filter(enhancement -> enhancement != null).collect(Collectors.toList());
        stage.setEnhancements(enhancements);
        talentStageRepository.save(stage);
        operationResult.setResultType(ResultType.SUCCESS);
        operationResult.setOperationType(OperationType.Update);
        operationResult.setCount(Integer.valueOf(count.get()));
        return operationResult;
    }
}
