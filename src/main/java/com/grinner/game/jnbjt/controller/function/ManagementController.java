package com.grinner.game.jnbjt.controller.function;

import com.grinner.game.jnbjt.pojo.ao.ManagementQuery;
import com.grinner.game.jnbjt.pojo.response.TableData;
import com.grinner.game.jnbjt.pojo.vo.composite.ActivityValueVO;
import com.grinner.game.jnbjt.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("management")
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    @PostMapping("detail/list")
    public TableData getDetailList(ManagementQuery managementQuery){
        List<ActivityValueVO> infoList = managementService.getDetailList(managementQuery);
        TableData tableData = new TableData();
        tableData.setCode("");
        tableData.setMsg("");
        tableData.setData(infoList);
        tableData.setCount(infoList.size());
        return tableData;
    }
}
