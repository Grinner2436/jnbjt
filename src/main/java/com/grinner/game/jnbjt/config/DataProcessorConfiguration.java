package com.grinner.game.jnbjt.config;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DataProcessorConfiguration {

    /**
     * 使用fastjson的接口序列化Enum类型，使得SpringConverter返回枚举时和fastjson行为一致
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer enumCustomizer(){
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(JSONSerializable.class, new JsonSerializer<JSONSerializable>() {
            @Override
            public void serialize(JSONSerializable sourceObject, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String jsonString = sourceObject.toString();
                gen.writeRawValue(jsonString);
            }
        });
    }
}
