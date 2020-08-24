package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.TalentStageRepository;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.pojo.ao.EnhancementAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TalentService {

    @Autowired
    private TalentStageRepository talentStageRepository;

    @Autowired
    private AssetRepository assetRepository;

    public void saveTalentStageEnhancements(Integer stageId, List<EnhancementAO> enhancements){
        Optional<TalentStage> talentStageOptional = talentStageRepository.findById(stageId);
        if(!talentStageOptional.isPresent()){
            return;
        }
        TalentStage talentStage = talentStageOptional.get();
        enhancements.stream().map(enhancementAO -> {
            String assetName = enhancementAO.getAssetName();
            Asset asset = assetRepository.findByName(assetName);
            Enhancement enhancement = new GenericEnhancement();
            enhancement.setAsset(asset);
            enhancement.setOperation(enhancementAO.getEnhancementOperation());
        })
    }
}
