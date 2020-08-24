package com.grinner.game.jnbjt.config;

import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.BuildingRepository;
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

    @PostConstruct
    public void init(){
        {
            Asset time = assetRepository.findByName("时间");
            if(time == null){
                time = new Asset();
                time.setId(Integer.valueOf(0));
                time.setName("时间");
                time.setValue(Integer.valueOf(0));
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
                anything.setValue(Integer.valueOf(-1));
                assetRepository.save(anything);
            }
            Asset.ANYTHING = anything;
        }
        {
            Building anyBuilding = null;
            Optional<Building> optionalBuilding = buildingRepository.findById(Integer.valueOf(-1));
            if(!optionalBuilding.isPresent()){
                anyBuilding = new Building();
                anyBuilding.setId(Integer.valueOf(-1));
                anyBuilding.setName("任意建筑");
                buildingRepository.save(anyBuilding);
            }else {
                anyBuilding = optionalBuilding.get();
            }
            Building.ANY_BUILDING = anyBuilding;
        }
    }
}
