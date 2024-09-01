package com.akkarimzai.Profiles;

import com.akkarimzai.Entities.MapPoint;
import com.akkarimzai.Profiles.Serializers.MapPointSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MapPointXmlMapper extends XmlMapper {
    public MapPointXmlMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(MapPoint.class, new MapPointSerializer());
        registerModule(module);
    }
}
