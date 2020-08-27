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
                enhancementVO.setTalentStageId(talentStage.getId());

                EnhancementQualificationVO qualificationVO = new EnhancementQualificationVO();
                qualificationVO.setActivityId(enhancement.getQualification().getActivity().getId());
                qualificationVO.setBuildingId(enhancement.getQualification().getBuilding().getId());
                qualificationVO.setProfession(enhancement.getQualification().getProfession());
                qualificationVO.setJob(enhancement.getQualification().getJob());
                enhancementVO.setQualification(qualificationVO);

                List<EnhancementOperationVO> enhancementOperationList = enhancement.getOperations().stream().map(operationItem -> {
                    EnhancementOperationVO operationVO = new EnhancementOperationVO();
                    operationVO.setOperand(operationItem.getOperand());
                    operationVO.setOperation(operationItem.getOperation());
                    operationVO.setOperationTarget(operationItem.getOperationTarget());
                    operationVO.setAssetId(operationItem.getAsset().getId());
                    return operationVO;
                }).collect(Collectors.toList());
                enhancementVO.setOperations(enhancementOperationList);
                return enhancementVO;
            }).collect(Collectors.toList());
            talentStageVO.setEnhancements(enhancements);
            return talentStageVO;
        }).collect(Collectors.toList());
        return result;
    }

    @Transactional
    public OperationResult setResidentTalentList(Integer stageId, List<EnhancementVO> enhancementList){
        OperationResult operationResult = new OperationResult();
        Optional<TalentStage> stageOptional = talentStageRepository.findById(stageId);
        if(!stageOptional.isPresent()){
            operationResult.setResultType(ResultType.FAILED);
            operationResult.setReason("Stage 不存在");
            return operationResult;
        }
        TalentStage stage = stageOptional.get();
        AtomicInteger count = new AtomicInteger();
        List<Enhancement> enhancements = enhancementList.stream().map(enhancementVO -> {
            EnhancementQualificationVO qualificationVO = enhancementVO.getQualification();
            Integer activityId = qualificationVO.getActivityId();
            Integer buildingId = qualificationVO.getBuildingId();
            Profession profession = qualificationVO.getProfession();
            Job job = qualificationVO.getJob();
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

            List<EnhancementOperationVO> operations = enhancementVO.getOperations();
            List<EnhancementOperation> enhancementOperations = operations.stream().map(enhancementOperationVO -> {
                Integer assetId = enhancementOperationVO.getAssetId();
                OperationTarget operationTarget = enhancementOperationVO.getOperationTarget();
                Operation operation = enhancementOperationVO.getOperation();
                Integer operand = enhancementOperationVO.getOperand();
                if(assetId == null || !assetRepository.existsById(assetId) || operationTarget == null || operation == null || operand == null){
                    return null;
                }
                EnhancementOperation enhancementOperation = new EnhancementOperation();
                enhancementOperation.setOperand(enhancementOperationVO.getOperand());
                enhancementOperation.setOperation(enhancementOperationVO.getOperation());
                Asset asset = assetRepository.getOne(assetId);
                enhancementOperation.setAsset(asset);
                enhancementOperation.setOperationTarget(operationTarget);
                return enhancementOperation;
            }).filter(operation -> operation != null).collect(Collectors.toList());

            Integer enhancementId = enhancementVO.getId();
            Enhancement enhancement = null;
            if(enhancementId != null && enhancementRepository.existsById(enhancementId)){
                enhancement = enhancementRepository.getOne(enhancementId);
            }else {
                enhancement = new GenericEnhancement();
            }
            enhancement.setQualification(qualification);
            enhancement.setOperations(enhancementOperations);

            enhancement.setTalentStage(stage);
//            enhancementRepository.save(enhancement);
            count.incrementAndGet();
            return enhancement;
        }).filter(enhancement -> enhancement != null).collect(Collectors.toList());
        if(enhancementList.isEmpty()){
            Integer rows = enhancementRepository.deleteByTalentStage(stage);
            operationResult.setResultType(ResultType.SUCCESS);
            operationResult.setOperationType(OperationType.Delete);
            operationResult.setCount(rows);
        }else if(!enhancements.isEmpty()){
            enhancementRepository.saveAll(enhancements);
            operationResult.setResultType(ResultType.SUCCESS);
            operationResult.setOperationType(OperationType.Update);
            operationResult.setCount(Integer.valueOf(enhancements.size()));
        }
        return operationResult;
    }
}
