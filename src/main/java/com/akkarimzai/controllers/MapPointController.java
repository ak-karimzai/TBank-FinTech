package com.akkarimzai.controllers;

import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.akkarimzai.exceptions.ValidationException;
import com.akkarimzai.models.DeserializeFileDto;
import com.akkarimzai.services.MapPointService;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MapPointController {
    private final MapPointService mapPointService;

    public MapPointController(MapPointService mapPointService) {
        this.mapPointService = mapPointService;
    }

    public void deserialize(DeserializeFileDto readFileRequest)  {
        log.info("request {} accepted", readFileRequest);
        try {
            mapPointService.deserialize(readFileRequest);
            log.info("request {} deserialized successfully", readFileRequest);
        } catch (NotFoundException | ValidationException | DatabindException e) {
            log.error("An error occurred during deserializing {}: {}", readFileRequest, e.getMessage());
        }
    }
}
