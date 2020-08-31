package com.grinner.game.jnbjt.controller.entity;

import com.grinner.game.jnbjt.pojo.response.GroupedResidentTalent;
import com.grinner.game.jnbjt.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping("/list")
    public GroupedResidentTalent getAllResidents(){
        GroupedResidentTalent data = residentService.getAllResident();
        return data;
    }
}
