package com.akkarimzai.services;

import com.akkarimzai.entities.MapPoint;
import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.akkarimzai.exceptions.ValidationException;
import com.akkarimzai.models.DeserializeFileDto;
import com.akkarimzai.repositories.MapPointRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MapPointService {
    private final MapPointRepository mapPointRepository;
    private final ObjectMapper deserializer;
    private final ObjectMapper serializer;


    public MapPointService(MapPointRepository mapPointRepository, ObjectMapper deserializer, ObjectMapper serializer) {
        this.mapPointRepository = mapPointRepository;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public void deserialize(DeserializeFileDto deserializeFileRequest) throws NotFoundException, DatabindException, ValidationException {
        log.info("request {} received", deserializeFileRequest);
        if (deserializeFileRequest.getSrcPath().isEmpty() || deserializeFileRequest.getDstPath().isEmpty()) {
            log.error("invalid request {}", deserializeFileRequest);
            throw new ValidationException(
                    String.format("invalid request {%s}", deserializeFileRequest));
        }

        MapPoint mapPoint = mapPointRepository.load(deserializeFileRequest.getSrcPath(), this.deserializer);
        log.info("request {} object {} loaded", deserializeFileRequest, mapPoint);

        mapPointRepository.save(mapPoint, deserializeFileRequest.getDstPath(), this.serializer);
        log.info("request {} object {} deserialized successfully", deserializeFileRequest, mapPoint);
    }
}
