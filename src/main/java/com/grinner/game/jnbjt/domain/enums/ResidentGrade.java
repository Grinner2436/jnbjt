package com.grinner.game.jnbjt.domain.enums;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import lombok.Getter;

import java.lang.reflect.Type;

/**
 * 居民品质
 */
public enum  ResidentGrade  implements JSONSerializable {
    Legendary("天", 0),
    Excellent("侯",1),
    Wellknown("卿",2),
    Remarkable("士",3);

    @Getter
    private String description;
    private Integer level;

    ResidentGrade(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public static ResidentGrade getResidentGrade(String description) {
        for(ResidentGrade residentGrade : ResidentGrade.values()){
            if(residentGrade.description.equals(description)){
                return residentGrade;
            }
        }
        return Remarkable;
    }


    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Type type, int i) {
        jsonSerializer.write(toString());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",name());
        jsonObject.put("level",level);
        jsonObject.put("description",description);
        return jsonObject.toJSONString();
    }
}
