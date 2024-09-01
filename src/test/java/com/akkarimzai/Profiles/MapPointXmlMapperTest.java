package com.akkarimzai.Profiles;

import com.akkarimzai.Entities.Coordinate;
import com.akkarimzai.Entities.MapPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapPointXmlMapperTest {
    private MapPointXmlMapper mapPointXmlMapper;

    @BeforeEach
    void setUp() {
        mapPointXmlMapper = new MapPointXmlMapper();
    }

    @Test
    void mapPointToXml() throws JsonProcessingException {
        // Arrange
        MapPoint mapPoint = new MapPoint("mos", new Coordinate(33.222, 22.333));
        String actual = "<MapPoint><slug>mos</slug><coords><lat>33.222</lat><lon>22.333</lon></coords></MapPoint>";

        // Act
        String asserted = mapPointXmlMapper.writeValueAsString(mapPoint);

        // Assert
        assertEquals(actual, asserted);
    }
}