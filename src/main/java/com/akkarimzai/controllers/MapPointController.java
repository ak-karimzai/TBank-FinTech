package com.akkarimzai.controllers;

import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.akkarimzai.exceptions.ValidationException;
import com.akkarimzai.models.DeserializeFileDto;
import com.akkarimzai.services.MapPointService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapPointController {
    private final Logger logger = LogManager.getLogger(MapPointController.class.getName());
    private final MapPointService mapPointService;

    public MapPointController(MapPointService mapPointService) {
        this.mapPointService = mapPointService;
    }

    public void deserialize(DeserializeFileDto readFileRequest)  {
        logger.info("request {} accepted", readFileRequest);
        try {
            mapPointService.deserialize(readFileRequest);
            logger.info("request {} deserialized successfully", readFileRequest);
        } catch (NotFoundException | ValidationException | DatabindException e) {
            logger.error("An error occurred during deserializing {}: {}", readFileRequest, e.getMessage());
        }
    }
}
