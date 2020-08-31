package com.grinner.game.jnbjt.pojo.ao;

import lombok.Data;

import java.util.List;

@Data
public class ManagementQuery {
    private List<String> activityNames;
    private List<String> residentNames;
    private List<String> assetNames;
}
