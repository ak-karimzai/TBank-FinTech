package com.akkarimzai.Controllers;

import com.akkarimzai.Exceptions.DatabindException;
import com.akkarimzai.Exceptions.NotFoundException;
import com.akkarimzai.Exceptions.ValidationException;
import com.akkarimzai.Models.DeserializeFileDto;
import com.akkarimzai.Services.MapPointService;

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
