package com.grinner.game.jnbjt.domain.entity;

import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import com.grinner.game.jnbjt.domain.relation.ResidentProperty;
import com.grinner.game.jnbjt.domain.relation.TreasureProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源计算型增强
 */
@Data
@Entity
public class GenericEnhancement extends Enhancement{

    public void operate(Activity activity, ResidentProperty residentProperty, TreasureProperty treasureProperty) {
        //验证条件
        {
            if (qualification.getBuilding() != Building.ANY_BUILDING && qualification.getBuilding().getId().intValue() != activity.getBuilding().getId().intValue()){
                return;
            };
            if (qualification.getProfession() != Profession.Any && qualification.getProfession() != activity.getProfession()){
                return;
            };
            if (qualification.getJob()  != Job.Any && qualification.getJob() != activity.getJob()){
                return;
            };
        }
        //实施增强
        {
            Map<Asset, AssetProperty> sourceAssetProperty = null;
            Map<Asset, AssetProperty> targetAssetProperty = new HashMap<>();
            //影响项目的来源
            if (operation.getOperationTarget() == OperationTarget.Investment) {
                sourceAssetProperty = activity.getInvestment().getAssetProperties();
            } else if (operation.getOperationTarget() == OperationTarget.Profit) {
                sourceAssetProperty = activity.getProfit().getAssetProperties();
            }
            //所有项目生效
            if (asset.getId().intValue() == Asset.ANYTHING.getId().intValue()) {
                targetAssetProperty.putAll(sourceAssetProperty);
            } else {
                AssetProperty assetProperty = sourceAssetProperty.get(asset);
                if (assetProperty == null) {
                    assetProperty = new AssetProperty();
//                    assetProperty.setAsset(asset);
                    assetProperty.setAssetName(asset.getName());
                    assetProperty.setAmount(Integer.valueOf(0));
                }
                targetAssetProperty.put(asset, assetProperty);
            }

            //应用增强
            targetAssetProperty.forEach((asset, assetProperty) -> {
                if (operation.getOperation() == Operation.Add) {
                    assetProperty.setAmount(assetProperty.getAmount() + operation.getOperand());
                } else if (operation.getOperation() == Operation.Minus) {
                    assetProperty.setAmount(assetProperty.getAmount() - operation.getOperand());
                } else if (operation.getOperation() == Operation.Multiply) {
                    assetProperty.setAmount(assetProperty.getAmount() * operation.getOperand());
                } else if (operation.getOperation() == Operation.Devide) {
                    assetProperty.setAmount(assetProperty.getAmount() / operation.getOperand());
                }
            });
        }
    }
}
