package com.grinner.game.jnbjt.core;

import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import com.grinner.game.jnbjt.domain.relation.ResidentProperty;
import com.grinner.game.jnbjt.domain.relation.TreasureProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenericActivityProcessor implements ActivityProcessor {
    @Override
    public void compute(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty) {
        //初始化人物属性值
        Map<Profession, Integer> finalAttrValue = new HashMap<>();
        residentProperty.getAttributeValues().forEach((profession, attributeProperty) -> {
            finalAttrValue.put(profession, attributeProperty.getAttributeVal());
        });

        //使用珍宝给人物增强属性
        if (treasureProperty != null){
            //珍宝天赋
            TalentStage talentStage = treasureProperty.getTalentStage();
            List<Enhancement> enhancements = talentStage.getEnhancements();
            enhancements.forEach(enhancement -> {
                enhancement.operate(activity, residentProperty, treasureProperty);
            });
            //经过一些增强处理

            //珍宝属性
            Map<Profession, Integer> treasureAttrValue  = treasureProperty.getAttributeValues();
            treasureAttrValue.forEach((profession, treasureValue) -> {
                Integer value = finalAttrValue.get(profession);
                value = value + treasureValue;
                treasureAttrValue.put(profession, Integer.valueOf(value));
            });
        }

        //使用属性影响活动成本
        Profession profession  = activity.getProfession();
        if(profession == Profession.Adventure || profession == Profession.Build){
            Investment activityInvestment = activity.getInvestment();
            Map<Asset, AssetProperty> assetProperties = activityInvestment.getAssetProperties();
            AssetProperty timeAssetProperty = assetProperties.get(Asset.TIME);
            //属性减少百分比
            Integer value = finalAttrValue.get(profession);
            double percent = value.intValue() / 15;

            //影响属性值
            double amount = timeAssetProperty.getAmount();
            timeAssetProperty.setAmount(amount * (1D - percent / 100D));
        }else if(profession == Profession.Farm || profession == Profession.Craft || profession == Profession.Finance){
            Profit profit = activity.getProfit();
            Map<Asset, AssetProperty> assetProperties = profit.getAssetProperties();
            assetProperties.forEach((asset, assetProperty) -> {
                //属性减少百分比
                Integer value = finalAttrValue.get(profession);
                double percent = value.intValue() / 15D;

                //影响属性值
                double amount = assetProperty.getAmount();
                assetProperty.setAmount(amount * (1D + percent / 100D));
            });
        }

        //应用天赋的增强
        //人物天赋
        TalentStage talentStage = residentProperty.getTalentStage();
        List<Enhancement> enhancements = talentStage.getEnhancements();
        enhancements.forEach(enhancement -> {
            enhancement.operate(activity, residentProperty, treasureProperty);
        });
    }
}
