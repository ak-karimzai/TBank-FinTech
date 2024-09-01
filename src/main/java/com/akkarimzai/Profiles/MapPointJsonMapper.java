package com.akkarimzai.Profiles;

import com.akkarimzai.Entities.MapPoint;
import com.akkarimzai.Profiles.Deserializers.MapPointDeserializer;
import com.akkarimzai.Profiles.Serializers.MapPointSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapPointJsonMapper extends ObjectMapper {
    public MapPointJsonMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MapPoint.class, new MapPointDeserializer());
        registerModule(module);
    }
}
