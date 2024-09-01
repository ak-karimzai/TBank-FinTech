package com.akkarimzai.profiles.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.akkarimzai.entities.MapPoint;

import java.io.IOException;

public class MapPointSerializer extends StdSerializer<MapPoint> {
    public MapPointSerializer() {
        this(null);
    }

    public MapPointSerializer(Class<MapPoint> t) {
        super(t);
    }

    @Override
    public void serialize(MapPoint mapPoint, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("slug", mapPoint.getSlug());

        jsonGenerator.writeObjectFieldStart("coords");
        jsonGenerator.writeObjectField(
                "lat", mapPoint.getCoordinate().getLatitude());
        jsonGenerator.writeObjectField(
                "lon", mapPoint.getCoordinate().getLongitude());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}
