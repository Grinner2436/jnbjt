package com.grinner.game.jnbjt.controller;

import com.grinner.game.jnbjt.pojo.ao.AssetAO;
import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.vo.AssetVO;
import com.grinner.game.jnbjt.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/value/list")
    public List<AssetVO> getAllAssets(){
        List<AssetVO> data = assetService.getAllAssets();
        return data;
    }

    @PostMapping("/value")
    public OperationResult setValues(AssetAO assetAO){
        OperationResult data = assetService.setValues(assetAO.getAssetList());
        return data;
    }
}
