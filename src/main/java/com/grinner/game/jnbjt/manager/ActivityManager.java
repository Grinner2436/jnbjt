package com.grinner.game.jnbjt.manager;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.RevitalizationRepository;
import com.grinner.game.jnbjt.dao.jpa.StatueRepository;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActivityManager {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StatueRepository statueRepository;

    @Autowired
    private RevitalizationRepository revitalizationRepository;

    /**
     * 官府活动
     * @return
     */
    @Cacheable("revitalized-activities")
    public List<Activity> getRevitalizedActivities(){
        List<Activity> result = new ArrayList<>();
        List<Revitalization> revitalizations = revitalizationRepository.findAll();
        if(revitalizations != null){
            revitalizations.forEach(revitalization -> {
                Map<Activity, Integer> activities = revitalization.getActivities();
                activities.forEach(((activitySource, amountDelta) -> {
                    Activity activityCopy = new Activity();
                    BeanUtils.copyProperties(activitySource,activityCopy);
                    Building buildingSource = activitySource.getBuilding();
                    Building buildingCopy = new Building();
                    BeanUtils.copyProperties(buildingSource, buildingCopy);
                    buildingCopy.setStateCapitals(null);
                    activityCopy.setBuilding(buildingCopy);
                    {
                        Investment investmentSource = activitySource.getInvestment();
                        Investment investmentCopy = new Investment();
                        BeanUtils.copyProperties(investmentSource, investmentCopy);
                        investmentCopy.setActivity(null);
                        Map<Asset, AssetProperty> assetPropertiesSource = investmentSource.getAssetProperties();
                        Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                        assetPropertiesSource.forEach((asset, assetProperty) -> {
                            Asset assetCopy = new Asset();
                            BeanUtils.copyProperties(asset, assetCopy);
                            AssetProperty assetPropertyCopy = new AssetProperty();

                            BeanUtils.copyProperties(asset, assetCopy);
                            assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                        });
                        investmentCopy.setAssetProperties(assetPropertiesCopy);
                        activityCopy.setInvestment(investmentCopy);
                    }
                    {
                        Profit profitSource = activitySource.getProfit();
                        Profit profitCopy = new Profit();
                        BeanUtils.copyProperties(profitSource, profitCopy);
                        profitCopy.setActivity(null);
                        Map<Asset, AssetProperty> assetPropertiesSource = profitSource.getAssetProperties();
                        Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                        assetPropertiesSource.forEach((asset, assetProperty) -> {
                            Asset assetCopy = new Asset();
                            BeanUtils.copyProperties(asset, assetCopy);
                            AssetProperty assetPropertyCopy = new AssetProperty();

                            Double amount = assetProperty.getAmount() + amountDelta;
                            assetPropertyCopy.setAmount(amount);
                            assetPropertyCopy.setAssetName(asset.getName());
                            assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                        });
                        profitCopy.setAssetProperties(assetPropertiesCopy);
                        activityCopy.setProfit(profitCopy);
                    }
                    activityCopy.setDescription("（" + revitalization.getName() + "）" + activityCopy.getDescription());
                    result.add(activityCopy);
                }));
            });
        }
        return result;
    }

    /**
     * 雕像加成
     * @return
     */
    @Cacheable("flourished-activities")
    public List<Activity> getFlourishedActivities(){
        List<Activity> result = new ArrayList<>();
        List<Statue> statues = statueRepository.findAll();
        if(statues != null){
            statues.forEach(statue -> {
                List<Building> buildings = statue.getBuildings();
                buildings.forEach(building -> {
                    List<Activity> activities = activityRepository.findAllByBuilding(building);
                    activities.forEach(activitySource -> {
                        if(activitySource.getProfession() == Profession.Build || activitySource.getProfession() == Profession.Adventure){
                            return;
                        }
                        Activity activityCopy = new Activity();
                        BeanUtils.copyProperties(activitySource,activityCopy);
                        Building buildingSource = activitySource.getBuilding();
                        Building buildingCopy = new Building();
                        BeanUtils.copyProperties(buildingSource, buildingCopy);
                        buildingCopy.setStateCapitals(null);
                        activityCopy.setBuilding(buildingCopy);
                        {
                            Investment investmentSource = activitySource.getInvestment();
                            Investment investmentCopy = new Investment();
                            BeanUtils.copyProperties(investmentSource, investmentCopy);
                            investmentCopy.setActivity(null);
                            Map<Asset, AssetProperty> assetPropertiesSource = investmentSource.getAssetProperties();
                            Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                            assetPropertiesSource.forEach((asset, assetProperty) -> {
                                Asset assetCopy = new Asset();
                                BeanUtils.copyProperties(asset, assetCopy);
                                AssetProperty assetPropertyCopy = new AssetProperty();

                                BeanUtils.copyProperties(asset, assetCopy);
                                assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                            });
                            investmentCopy.setAssetProperties(assetPropertiesCopy);
                            activityCopy.setInvestment(investmentCopy);
                        }
                        {
                            Profit profitSource = activitySource.getProfit();
                            Profit profitCopy = new Profit();
                            BeanUtils.copyProperties(profitSource, profitCopy);
                            profitCopy.setActivity(null);
                            Map<Asset, AssetProperty> assetPropertiesSource = profitSource.getAssetProperties();
                            Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                            assetPropertiesSource.forEach((asset, assetProperty) -> {
                                Asset assetCopy = new Asset();
                                BeanUtils.copyProperties(asset, assetCopy);
                                AssetProperty assetPropertyCopy = new AssetProperty();

                                Double amount = assetProperty.getAmount() * (1D + statue.getEffectValue());
                                assetPropertyCopy.setAmount(amount);
                                assetPropertyCopy.setAssetName(asset.getName());
                                assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                            });
                            profitCopy.setAssetProperties(assetPropertiesCopy);
                            activityCopy.setProfit(profitCopy);
                        }
                        activityCopy.setDescription("（" + statue.getName() + "）" + activityCopy.getDescription());
                        result.add(activityCopy);
                    });
                });
            });
        }
        return result;
    }

    /**
     * 官府+雕像加成
     * @return
     */
    @Cacheable("revitalized-flourished-activities")
    public List<Activity> getRevitalizedAndFlourishedActivities(){
        List<Activity> result = new ArrayList<>();
        List<Statue> statues = statueRepository.findAll();
        if(statues != null){
            statues.forEach(statue -> {
                List<Building> buildings = statue.getBuildings();
                buildings.forEach(building -> {
                    List<Activity> activities = getRevitalizedActivities(building);
                    activities.forEach(activitySource -> {
                        if(activitySource.getProfession() == Profession.Build || activitySource.getProfession() == Profession.Adventure){
                            return;
                        }

                        Activity activityCopy = new Activity();
                        BeanUtils.copyProperties(activitySource,activityCopy);

                        Building buildingSource = activitySource.getBuilding();
                        Building buildingCopy = new Building();
                        BeanUtils.copyProperties(buildingSource, buildingCopy);
                        buildingCopy.setStateCapitals(null);
                        activityCopy.setBuilding(buildingCopy);
                        {
                            Investment investmentSource = activitySource.getInvestment();
                            Investment investmentCopy = new Investment();
                            BeanUtils.copyProperties(investmentSource, investmentCopy);
                            investmentCopy.setActivity(null);
                            Map<Asset, AssetProperty> assetPropertiesSource = investmentSource.getAssetProperties();
                            Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                            assetPropertiesSource.forEach((asset, assetProperty) -> {
                                Asset assetCopy = new Asset();
                                BeanUtils.copyProperties(asset, assetCopy);
                                AssetProperty assetPropertyCopy = new AssetProperty();

                                BeanUtils.copyProperties(asset, assetCopy);
                                assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                            });
                            investmentCopy.setAssetProperties(assetPropertiesCopy);
                            activityCopy.setInvestment(investmentCopy);
                        }
                        {
                            Profit profitSource = activitySource.getProfit();
                            Profit profitCopy = new Profit();
                            BeanUtils.copyProperties(profitSource, profitCopy);
                            profitCopy.setActivity(null);
                            Map<Asset, AssetProperty> assetPropertiesSource = profitSource.getAssetProperties();
                            Map<Asset, AssetProperty> assetPropertiesCopy = new HashMap<>();
                            assetPropertiesSource.forEach((asset, assetProperty) -> {
                                Asset assetCopy = new Asset();
                                BeanUtils.copyProperties(asset, assetCopy);
                                AssetProperty assetPropertyCopy = new AssetProperty();

                                Double amount = assetProperty.getAmount() * (1D + statue.getEffectValue());
                                assetPropertyCopy.setAmount(amount);
                                assetPropertyCopy.setAssetName(asset.getName());
                                assetPropertiesCopy.put(assetCopy, assetPropertyCopy);
                            });
                            profitCopy.setAssetProperties(assetPropertiesCopy);
                            activityCopy.setProfit(profitCopy);
                        }

                        activityCopy.setDescription("（" + statue.getName() + "）" + activityCopy.getDescription());
                        result.add(activityCopy);
                    });
                });
            });
        }
        return result;
    }

    private List<Activity> getRevitalizedActivities(Building building){
        List<Activity> allRevitalizedActivities = getRevitalizedActivities();
        List<Activity> buildingActivities = allRevitalizedActivities.stream()
                .filter(activity -> activity.getBuilding().getId().intValue() == building.getId().intValue())
                .collect(Collectors.toList());
        return buildingActivities;
    }
}
