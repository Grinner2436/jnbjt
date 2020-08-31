package com.grinner.game.jnbjt.config;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grinner.game.jnbjt.domain.entity.Asset;

import java.io.IOException;

public class MapJsonToAsset extends KeyDeserializer{

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Asset deserializeKey(String json, DeserializationContext deserializationContext) throws IOException {
        Asset result = objectMapper.readValue(json, Asset.class);
        return result;
    }
}
