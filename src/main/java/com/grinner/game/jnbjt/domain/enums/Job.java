package com.grinner.game.jnbjt.domain.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

/**
 * 工作
 */
public enum Job implements JSONSerializable {
    CreateBuilding("建造新建筑"),
    UpgradeBuilding("升级建筑"),
    AnyJob("任意");

    private String name;

    Job(String name){
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
