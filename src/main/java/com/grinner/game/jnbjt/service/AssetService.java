package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.domain.entity.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<String> getAllAssetNames(){
        List<Asset> residents =  assetRepository.findAll();
        List<String> assetNames = residents.stream().map(Asset::getName).collect(Collectors.toList());
        return assetNames;
    }
}
