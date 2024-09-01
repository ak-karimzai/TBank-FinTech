package com.akkarimzai.profiles;

import com.akkarimzai.entities.MapPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapPointJsonMapperTest {
    private MapPointJsonMapper mapPointJsonMapper = new MapPointJsonMapper();

    @BeforeEach
    void setUp() {
        mapPointJsonMapper = new MapPointJsonMapper();
    }

    @Test
    void jsonToMapPoint() throws JsonProcessingException {
        // Arrange
        String json = """
               {
                  "slug": "spb",
                  "coords": {
                    "lat": 59.939095,
                    "lon": 30.315868
                  }
               }
               """;
        double epsilon = 0.000001d;


        // Act
        MapPoint point = mapPointJsonMapper.readValue(json, MapPoint.class);

        // Assert
        assertNotNull(point);
        assertEquals("spb", point.getSlug());
        assertNotNull(point.getCoordinate());
        assertEquals(point.getCoordinate().getLatitude(), 59.939095, epsilon);
        assertEquals(point.getCoordinate().getLongitude(), 30.315868, epsilon);
    }
}