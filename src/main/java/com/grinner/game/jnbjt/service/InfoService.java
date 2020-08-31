package com.grinner.game.jnbjt.service;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.ActivityRepository;
import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.dao.jpa.BuildingRepository;
import com.grinner.game.jnbjt.dao.jpa.ResidentRepository;
import com.grinner.game.jnbjt.domain.entity.Activity;
import com.grinner.game.jnbjt.domain.entity.Asset;
import com.grinner.game.jnbjt.domain.entity.Building;
import com.grinner.game.jnbjt.domain.entity.Resident;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.pojo.vo.ActivityVO;
import com.grinner.game.jnbjt.pojo.vo.AssetVO;
import com.grinner.game.jnbjt.pojo.vo.BuildingVO;
import com.grinner.game.jnbjt.pojo.vo.ResidentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ResidentRepository residentRepository;

    public JSONObject getMetaInfos(){
        JSONObject result = new JSONObject();
        List<Activity> activities = activityRepository.findAll();
        List<ActivityVO> activityList = activities.stream().map(activity -> {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setDescription(activity.getDescription());
            return activityVO;
        }).collect(Collectors.toList());
        result.put("activities", activityList);

        List<Building> buildings = buildingRepository.findAll();
        List<BuildingVO> buildingList= buildings.stream().map(building -> {
            BuildingVO buildingVO = new BuildingVO();
            buildingVO.setId(building.getId());
            buildingVO.setName(building.getName());
            return buildingVO;
        }).collect(Collectors.toList());
        result.put("buildings", buildingList);

        List<Asset> assets = assetRepository.findAll();
        List<AssetVO> assetList = assets.stream().map(asset -> {
            AssetVO assetVO = new AssetVO();
            assetVO.setId(asset.getId());
            assetVO.setName(asset.getName());
            return assetVO;
        }).collect(Collectors.toList());
        result.put("assets", assetList);

        List<Resident> residents = residentRepository.findAll();
        List<ResidentVO> residentList = residents.stream().map(resident -> {
            ResidentVO residentVO = new ResidentVO();
            residentVO.setId(resident.getId());
            residentVO.setName(resident.getName());
            return residentVO;
        }).collect(Collectors.toList());
        result.put("residents", residentList);

        result.put("professions", Profession.values());
        result.put("jobs", Job.values());
        result.put("targets", OperationTarget.values());
        result.put("operations", Operation.values());
        return result;
    }
}
