package com.akkarimzai.Services;

import com.akkarimzai.Entities.MapPoint;
import com.akkarimzai.Exceptions.DatabindException;
import com.akkarimzai.Exceptions.NotFoundException;
import com.akkarimzai.Exceptions.ValidationException;
import com.akkarimzai.Models.DeserializeFileDto;
import com.akkarimzai.Repositories.MapPointRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapPointService {
    private final MapPointRepository mapPointRepository;
    private final ObjectMapper deserializer;
    private final ObjectMapper serializer;

    private final Logger logger = LogManager.getLogger(MapPointService.class.getName());

    public MapPointService(MapPointRepository mapPointRepository, ObjectMapper deserializer, ObjectMapper serializer) {
        this.mapPointRepository = mapPointRepository;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public void deserialize(DeserializeFileDto deserializeFileRequest) throws NotFoundException, DatabindException, ValidationException {
        logger.info("request {} received", deserializeFileRequest);
        if (deserializeFileRequest.getSrcPath().isEmpty() || deserializeFileRequest.getDstPath().isEmpty()) {
            logger.error("invalid request {}", deserializeFileRequest);
            throw new ValidationException(
                    String.format("invalid request {%s}", deserializeFileRequest));
        }

        MapPoint mapPoint = mapPointRepository.load(deserializeFileRequest.getSrcPath(), this.deserializer);
        logger.info("request {} object {} loaded", deserializeFileRequest, mapPoint);

        mapPointRepository.save(mapPoint, deserializeFileRequest.getDstPath(), this.serializer);
        logger.info("request {} object {} deserialized successfully", deserializeFileRequest, mapPoint);
    }
}
