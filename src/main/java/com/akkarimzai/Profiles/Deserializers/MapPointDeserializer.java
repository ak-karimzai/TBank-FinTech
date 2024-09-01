package com.akkarimzai.Profiles.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.akkarimzai.Entities.Coordinate;
import com.akkarimzai.Entities.MapPoint;

import java.io.IOException;

public class MapPointDeserializer extends StdDeserializer<MapPoint> {
    public MapPointDeserializer() {
        this(null);
    }

    public MapPointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MapPoint deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode coordinateNode = node.get("coords");

        String slug = node.get("slug").asText();
        Double latitude = coordinateNode.get("lat").asDouble();
        Double longitude = coordinateNode.get("lon").asDouble();
        return new MapPoint(slug, new Coordinate(latitude, longitude));
    }
}
