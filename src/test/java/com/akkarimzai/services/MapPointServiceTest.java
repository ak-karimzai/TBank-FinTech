package com.akkarimzai.services;

import com.akkarimzai.entities.Coordinate;
import com.akkarimzai.entities.MapPoint;
import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.akkarimzai.exceptions.ValidationException;
import com.akkarimzai.models.DeserializeFileDto;
import com.akkarimzai.profiles.MapPointJsonMapper;
import com.akkarimzai.profiles.MapPointXmlMapper;
import com.akkarimzai.repositories.MapPointRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapPointServiceTest {
    @Mock
    private MapPointRepository mapPointRepository;

    @InjectMocks
    private MapPointService mapPointService;

    @BeforeEach
    void setUp() {
        mapPointService = new MapPointService(
                mapPointRepository, new MapPointJsonMapper(), new MapPointXmlMapper());
    }

    @Test
    void mapPointSuccessfullyDeserialized() throws DatabindException, ValidationException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");

        MapPoint mapPoint = new MapPoint("mos", new Coordinate(33.222, 22.333));
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenReturn(mapPoint);
        Mockito.doNothing().when(mapPointRepository).save(eq(mapPoint), eq(request.getDstPath()), any(ObjectMapper.class));


        // Act && Assert
        mapPointService.deserialize(request);
    }

    @Test
    void requestSrcPathIsEmpty() {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("", "dst");

        // Act && Assert
        assertThrows(ValidationException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void requestDstPathIsEmpty() {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "");

        // Act && Assert
        assertThrows(ValidationException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void srcFileNotExist() throws DatabindException, ValidationException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenThrow(NotFoundException.class);

        // Act && Assert
        assertThrows(NotFoundException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void cannotReadFileContent() throws DatabindException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenThrow(DatabindException.class);

        // Act && Assert
        assertThrows(DatabindException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void cannotDeserializeObject() throws DatabindException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");

        MapPoint mapPoint = new MapPoint("mos", new Coordinate(33.222, 22.333));
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenReturn(mapPoint);
        Mockito.doThrow(DatabindException.class).when(mapPointRepository).save(eq(mapPoint), eq(request.getDstPath()), any(ObjectMapper.class));

        // Act && Assert
        assertThrows(DatabindException.class, () -> mapPointService.deserialize(request));
    }
}