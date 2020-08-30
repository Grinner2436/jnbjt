package com.grinner.game.jnbjt.manager;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.RevitalizationRepository;
import com.grinner.game.jnbjt.dao.jpa.StatueRepository;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.relation.AssetProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
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
                activities.forEach(((activity, amountDelta) -> {
                    Map<Asset, AssetProperty> assetProperties = activity.getProfit().getAssetProperties();
                    assetProperties.forEach((asset, assetProperty) -> {
                        Double amount = assetProperty.getAmount() + amountDelta;
                        assetProperty.setAmount(amount);
                    });
                    result.add(activity);
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
                    activities.forEach(activity -> {
                        Map<Asset, AssetProperty> assetProperties = activity.getProfit().getAssetProperties();
                        assetProperties.forEach((asset, assetProperty) -> {
                            Double amount = assetProperty.getAmount() * (1D + statue.getEffectValue());
                            assetProperty.setAmount(amount);
                        });
                        result.add(activity);
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
                    activities.forEach(activity -> {
                        Map<Asset, AssetProperty> assetProperties = activity.getProfit().getAssetProperties();
                        assetProperties.forEach((asset, assetProperty) -> {
                            Double amount = assetProperty.getAmount() * (1D + statue.getEffectValue());
                            assetProperty.setAmount(amount);
                        });
                        result.add(activity);
                    });
                });
            });
        }
        return result;
    }

    @Cacheable("revitalized-building-activities")
    public List<Activity> getRevitalizedActivities(Building building){
        List<Activity> allRevitalizedActivities = getRevitalizedActivities();
        List<Activity> buildingActivities = allRevitalizedActivities.stream()
                .filter(activity -> activity.getBuilding().getId().intValue() == building.getId().intValue())
                .collect(Collectors.toList());
        return buildingActivities;
    }
}
