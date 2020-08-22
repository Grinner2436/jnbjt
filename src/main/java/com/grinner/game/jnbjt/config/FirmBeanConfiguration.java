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
            Asset time = null;
                Optional<Asset> optionalAsset = assetRepository.findById(Integer.valueOf(0));
                if(!optionalAsset.isPresent()){
                    time = new Asset();
                    time.setId(Integer.valueOf(0));
                    time.setName("时间");
                    time.setValue(Integer.valueOf(0));
                    assetRepository.save(time);
                }else {
                    time = optionalAsset.get();
                }
                Asset.TIME = time;
        }
        {
            Asset anything = null;
            Optional<Asset> optionalAsset = assetRepository.findById(Integer.valueOf(-1));
            if(!optionalAsset.isPresent()){
                anything = new Asset();
                anything.setId(Integer.valueOf(-1));
                anything.setName("任何资源");
                anything.setValue(Integer.valueOf(-1));
                assetRepository.save(anything);
            }else {
                anything = optionalAsset.get();
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
