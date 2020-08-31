package com.grinner.game.jnbjt.pojo.response;

import lombok.Data;

import java.util.List;

@Data
public class TableData<T> {
    private String code;
    private String msg;
    private Integer count;
    private List<T> data;
}
