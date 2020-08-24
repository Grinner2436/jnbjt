package com.grinner.game.jnbjt.response;

import lombok.Data;

import java.util.List;

@Data
public class LayUITableDataList {
    private String msg = "";
    private Integer code = Integer.valueOf(0);
    private Integer count;
    private List data;
}
