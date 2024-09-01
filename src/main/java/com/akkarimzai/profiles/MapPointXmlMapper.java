package com.akkarimzai.profiles;

import com.akkarimzai.entities.MapPoint;
import com.akkarimzai.profiles.Serializers.MapPointSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MapPointXmlMapper extends XmlMapper {
    public MapPointXmlMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(MapPoint.class, new MapPointSerializer());
        registerModule(module);
    }
}
