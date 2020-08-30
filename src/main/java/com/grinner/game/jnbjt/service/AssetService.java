package com.grinner.game.jnbjt.service;

import com.grinner.game.jnbjt.dao.jpa.AssetRepository;
import com.grinner.game.jnbjt.domain.entity.Asset;
import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.response.OperationType;
import com.grinner.game.jnbjt.pojo.vo.AssetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<String> getAllAssetNames(){
        List<Asset> assets =  assetRepository.findAll();
        List<String> assetNames = assets.stream().map(Asset::getName).collect(Collectors.toList());
        return assetNames;
    }

    public OperationResult setValues(List<AssetVO> assetList){
        AtomicInteger total = new AtomicInteger(0);
        assetList.forEach(assetVO -> {
            Integer id = assetVO.getId();
            Optional<Asset> assetOptional = assetRepository.findById(id);
            if(assetOptional.isPresent()){
                Asset asset = assetOptional.get();
                if(assetVO.getValue() != null){
                    asset.setValue(assetVO.getValue());
                    assetRepository.save(asset);
                    total.getAndIncrement();
                }
            }
        });
        OperationResult result = new OperationResult();
        result.setOperationType(OperationType.Update);
        result.setCount(total.get());
        return result;
    }
    public List<AssetVO> getAllAssets(){
        List<Asset> assets =  assetRepository.findAll();
        List<AssetVO> assetList = assets.stream().map(asset -> {
            AssetVO assetVO = new AssetVO();
            assetVO.setId(asset.getId());
            assetVO.setName(asset.getName());
            assetVO.setValue(asset.getValue());
            return assetVO;
        }).collect(Collectors.toList());
        return assetList;
    }
}
