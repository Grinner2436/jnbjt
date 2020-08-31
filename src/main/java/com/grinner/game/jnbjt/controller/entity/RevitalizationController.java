package com.grinner.game.jnbjt.controller.entity;

import com.grinner.game.jnbjt.pojo.response.OperationResult;
import com.grinner.game.jnbjt.pojo.vo.RevitalizationVO;
import com.grinner.game.jnbjt.service.RevitalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("revitalization")
public class RevitalizationController {

    @Autowired
    private RevitalizationService revitalizationService;

    @GetMapping("/list")
    public List<RevitalizationVO> getRevitalizationList(){
        List<RevitalizationVO> data = revitalizationService.getRevitalizationList();
        return data;
    }

    @GetMapping("/{id}")
    public RevitalizationVO getRevitalization(@PathVariable Integer id){
        RevitalizationVO data = revitalizationService.getRevitalizationDetail(id);
        return data;
    }

    @DeleteMapping("/{id}")
    public OperationResult delRevitalization(@PathVariable Integer id){
        OperationResult data = revitalizationService.delRevitalizationDetail(id);
        return data;
    }

    @PostMapping(path = {"","/"})
    public OperationResult setRevitalization(RevitalizationVO revitalizationAO){
        OperationResult data = revitalizationService.setRevitalization(revitalizationAO);
        return data;
    }
}
