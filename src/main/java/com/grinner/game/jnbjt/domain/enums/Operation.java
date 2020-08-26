package com.grinner.game.jnbjt.domain.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

public enum Operation implements JSONSerializable {
    Add("＋"),
    Minus("－"),
    Multiply("×"),
    Devide("÷");

    private String operator;

    Operation(String operator){
        this.operator = operator;
    }

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Type type, int i) {
        jsonSerializer.write(toString());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",name());
        jsonObject.put("operator",operator);
        return jsonObject.toJSONString();
    }
}
