package com.akkarimzai.Services;

import com.akkarimzai.Entities.Coordinate;
import com.akkarimzai.Entities.MapPoint;
import com.akkarimzai.Exceptions.DatabindException;
import com.akkarimzai.Exceptions.NotFoundException;
import com.akkarimzai.Exceptions.ValidationException;
import com.akkarimzai.Models.DeserializeFileDto;
import com.akkarimzai.Profiles.MapPointJsonMapper;
import com.akkarimzai.Profiles.MapPointXmlMapper;
import com.akkarimzai.Repositories.MapPointRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class MapPointServiceTest {
    private AutoCloseable autoCloseable;

    @Mock
    private MapPointRepository mapPointRepository;

    @InjectMocks
    private MapPointService mapPointService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mapPointService = new MapPointService(
                mapPointRepository, new MapPointJsonMapper(), new MapPointXmlMapper());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
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
    void requestSrcPathIsEmpty() throws DatabindException, ValidationException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("", "dst");

        // Act && Assert
        assertThrows(ValidationException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void requestDstPathIsEmpty() throws DatabindException, ValidationException, NotFoundException {
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
    void cannotReadFileContent() throws DatabindException, ValidationException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenThrow(DatabindException.class);

        // Act && Assert
        assertThrows(DatabindException.class, () -> mapPointService.deserialize(request));
    }

    @Test
    void cannotDeserializeObject() throws DatabindException, ValidationException, NotFoundException {
        // Arrange
        DeserializeFileDto request = new DeserializeFileDto("src", "dst");

        MapPoint mapPoint = new MapPoint("mos", new Coordinate(33.222, 22.333));
        when(mapPointRepository.load(eq(request.getSrcPath()), any(ObjectMapper.class))).thenReturn(mapPoint);
        Mockito.doThrow(DatabindException.class).when(mapPointRepository).save(eq(mapPoint), eq(request.getDstPath()), any(ObjectMapper.class));

        // Act && Assert
        assertThrows(DatabindException.class, () -> mapPointService.deserialize(request));
    }
}