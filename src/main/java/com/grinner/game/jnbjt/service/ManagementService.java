package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.ResidentRepository;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import com.grinner.game.jnbjt.domain.relation.AttributeProperty;
import com.grinner.game.jnbjt.manager.ActivityManager;
import com.grinner.game.jnbjt.pojo.vo.AssetPropertyVO;
import com.grinner.game.jnbjt.pojo.vo.AssetVO;
import com.grinner.game.jnbjt.pojo.vo.ProfitVO;
import com.grinner.game.jnbjt.pojo.vo.composite.ActivityValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManagementService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ActivityManager activityManager;

    public List<ActivityValueVO> getDetailList(){
        List<ActivityValueVO> result = new ArrayList<>();

        List<Activity> allActivities = new ArrayList<>();
        List<Activity> revitalizedActivities = activityManager.getRevitalizedActivities();
//        List<Activity> flourishedActivities = activityManager.getFlourishedActivities();
//        List<Activity> revitalizedAndFlourishedActivities = activityManager.getRevitalizedAndFlourishedActivities();
        allActivities.addAll(revitalizedActivities);
//        allActivities.addAll(flourishedActivities);
//        allActivities.addAll(revitalizedAndFlourishedActivities);

        List<Resident> residents = residentRepository.findAll();
        allActivities.forEach(activity -> {
            residents.forEach(resident -> {
                ActivityValueVO activityValueVO = new ActivityValueVO();
                activityValueVO.setActityName(activity.getDescription());
                activityValueVO.setResidentName(resident.getName());

                Map<Profession, AttributeProperty> attributeValues = resident.getMaxAttributes();
                Profession profession = activity.getProfession();
                AttributeProperty attributeProperty = attributeValues.get(profession);
                if(attributeProperty == null){
                    attributeProperty = new  AttributeProperty();
                }
                Integer attributeValue = attributeProperty.getAttributeVal();
                if(attributeValue == null){
                    attributeValue = Integer.valueOf(0);
                }
                int attributeVal = attributeValue;

                //属性减少时间成本
                if(profession == Profession.Build || profession == Profession.Adventure ){
                    Investment activityInvestment = activity.getInvestment();
                    Map<Asset, AssetProperty> assetProperties = activityInvestment.getAssetProperties();
                    AssetProperty timeAssetProperty = assetProperties.get(Asset.TIME);
                    //属性减少百分比
                    double percent = attributeVal / 15;

                    //影响属性值
                    double amount = timeAssetProperty.getAmount();
                    timeAssetProperty.setAmount(amount * (1D - percent / 100D));
                }else{//属性提高收益产量
                    Profit profit = activity.getProfit();
                    Map<Asset, AssetProperty> assetProperties = profit.getAssetProperties();
                    assetProperties.forEach((asset, assetProperty) -> {
                        //属性减少百分比
                        double percent = attributeVal / 15D;

                        //影响属性值
                        double amount = assetProperty.getAmount();
                        assetProperty.setAmount(amount * (1D + percent / 100D));
                    });
                }

                Talent talent = resident.getTalent();
                talent.getStages().sort(Comparator.comparingInt(stage -> stage.getLevel()));
                TalentStage talentStage = talent.getStages().get(0);
                List<Enhancement> enhancements = talentStage.getEnhancements();
                enhancements.forEach(enhancement -> {
                    enhancement.operate(activity, resident);
                });

                ProfitVO profitVO = new ProfitVO();
                activityValueVO.setProfit(profitVO);
                List<AssetPropertyVO> profitAssets = new ArrayList<>();
                profitVO.setAssetProperties(profitAssets);

                double total = 0D;
                Map<Asset, AssetProperty> assetProperties = activity.getProfit().getAssetProperties();
                for (Asset asset : assetProperties.keySet()){
                    AssetProperty assetProperty = assetProperties.get(asset);
                    AssetPropertyVO assetPropertyVO = new AssetPropertyVO();
                    assetPropertyVO.setAssetName(assetProperty.getAssetName());
                    assetPropertyVO.setAmount(assetProperty.getAmount());
                    profitAssets.add(assetPropertyVO);
                    total += asset.getValue() * assetProperty.getAmount();
                }
                activityValueVO.setValue(Double.valueOf(total));
                result.add(activityValueVO);
            });
        });
        return result;
    }
}
