package com.grinner.game.jnbjt.controller;

import com.alibaba.fastjson.JSONObject;
import com.grinner.game.jnbjt.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("meta/list")
    public JSONObject getMetaInfoList(){
        JSONObject infoList = infoService.getMetaInfos();
        return infoList;
    }
}
