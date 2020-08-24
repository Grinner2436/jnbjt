package com.grinner.game.jnbjt.controller;

import com.grinner.game.jnbjt.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/name/list")
    public List<String> getAssetNameList(){
        List<String> data = assetService.getAllAssetNames();
        return data;
    }

}
