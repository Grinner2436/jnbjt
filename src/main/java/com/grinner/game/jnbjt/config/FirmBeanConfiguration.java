package com.grinner.game.jnbjt.config;

import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.BuildingRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Asset;
import com.grinner.game.jnbjt.domain.entity.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
public class FirmBeanConfiguration {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @PostConstruct
    public void init(){
        {
            Asset time = assetRepository.findByName("时间");
            if(time == null){
                time = new Asset();
                time.setId(Integer.valueOf(0));
                time.setName("时间");
                time.setValue(Double.valueOf(0));
                assetRepository.save(time);
            }
            Asset.TIME = time;
        }
        {
            Asset anything = assetRepository.findByName("任何资源");
            if(anything == null){
                anything = new Asset();
                anything.setId(Integer.valueOf(-1));
                anything.setName("任何资源");
                anything.setValue(Double.valueOf(-1));
                assetRepository.save(anything);
            }
            Asset.ANYTHING = anything;
        }
        {
            Building anyBuilding = buildingRepository.findByName("任意建筑");
            if(anyBuilding == null){
                anyBuilding = new Building();
                anyBuilding.setId(Integer.valueOf(-1));
                anyBuilding.setName("任意建筑");
                buildingRepository.save(anyBuilding);
            }
            Building.ANY_BUILDING = anyBuilding;
        }
        {
            Activity anyActivity = activityRepository.findByDescription("任何活动");
            if(anyActivity == null){
                anyActivity = new Activity();
                anyActivity.setId(Integer.valueOf(-1));
                anyActivity.setDescription("任何活动");
                activityRepository.save(anyActivity);
            }
            Activity.ANY_ACTIVITY = anyActivity;
        }
    }
}
