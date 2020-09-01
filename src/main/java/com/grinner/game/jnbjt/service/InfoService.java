package com.grinner.game.jnbjt.service;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.dao.jpa.*;
import com.grinner.game.jnbjt.domain.entity.*;
import com.grinner.game.jnbjt.domain.enums.Job;
import com.grinner.game.jnbjt.domain.enums.Operation;
import com.grinner.game.jnbjt.domain.enums.OperationTarget;
import com.grinner.game.jnbjt.domain.enums.Profession;
import com.grinner.game.jnbjt.domain.instance.AssetProperty;
import com.grinner.game.jnbjt.manager.ActivityManager;
import com.grinner.game.jnbjt.pojo.vo.ActivityVO;
import com.grinner.game.jnbjt.pojo.vo.AssetVO;
import com.grinner.game.jnbjt.pojo.vo.BuildingVO;
import com.grinner.game.jnbjt.pojo.vo.ResidentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private ActivityManager activityManager;

    public JSONObject getMetaInfos(){
        JSONObject result = new JSONObject();
        List<ActivityVO> activityWithStatue = new ArrayList<>();

        List<Activity> activities = activityRepository.findAll();
        List<ActivityVO> activityList = activities.stream().map(activity -> {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setDescription(activity.getDescription());
            return activityVO;
        }).collect(Collectors.toList());
        result.put("activities", activityList);
        List<ActivityVO> revitalizedActivities = activityManager.getRevitalizedActivities().stream().map(activity -> {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setDescription(activity.getDescription());
            return activityVO;
        }).collect(Collectors.toList());;
        List<ActivityVO> flourishedActivities = activityManager.getFlourishedActivities().stream().map(activity -> {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setDescription(activity.getDescription());
            return activityVO;
        }).collect(Collectors.toList());
        activityWithStatue.addAll(activityList);
        activityWithStatue.addAll(revitalizedActivities);
        activityWithStatue.addAll(flourishedActivities);
        result.put("activityList", activityWithStatue);

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
