package com.grinner.game.jnbjt.controller;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.response.LayUITableDataList;
import com.grinner.game.jnbjt.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping("/talent/list")
    public LayUITableDataList getResidentTalentList(){
        List<JSONObject> data = residentService.getAllResidentTalentStage();
        LayUITableDataList dataList = new LayUITableDataList();
        dataList.setCount(data.size());
        dataList.setData(data);
        return dataList;
    }
}
