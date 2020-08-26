package com.grinner.game.jnbjt.domain.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

/**
 * 职业
 */
public enum Profession implements JSONSerializable{
    Build("建造"),
    Farm("农牧"),
    Craft("制作"),
    Finance("理财"),
    Adventure("探险"),
    Any("任意职业");

    private String name;

    Profession(String name){
        this.name = name;
    }

    public static Profession getProfession(String name) {
        for(Profession profession : Profession.values()){
            if(profession.name.equals(name)){
                return profession;
            }
        }
        return Farm;
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
