package com.grinner.game.jnbjt.domain.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

public enum OperationTarget implements JSONSerializable {
    Profit("获利"),
    Investment("投资");

    private String name;

    OperationTarget(String name){
        this.name = name;
    }

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Type type, int i) {
        jsonSerializer.write(toString());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",name());
        jsonObject.put("name",name);
        return jsonObject.toJSONString();
    }
}
