package com.akkarimzai.profiles;

import com.akkarimzai.entities.MapPoint;
import com.akkarimzai.profiles.Deserializers.MapPointDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapPointJsonMapper extends ObjectMapper {
    public MapPointJsonMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MapPoint.class, new MapPointDeserializer());
        registerModule(module);
    }
}
