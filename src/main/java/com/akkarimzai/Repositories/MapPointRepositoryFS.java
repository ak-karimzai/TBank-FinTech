package com.akkarimzai.Repositories;

import com.akkarimzai.Exceptions.DatabindException;
import com.akkarimzai.Exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.akkarimzai.Entities.MapPoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MapPointRepositoryFS implements MapPointRepository {
    private final Logger logger = LogManager.getLogger(MapPointRepositoryFS.class.getName());

    @Override
    public void save(MapPoint mapPoint, String path, ObjectMapper mapper) throws DatabindException {
        logger.info("saving map point {} to file {}", mapPoint, path);

        try (var fos = new FileOutputStream(path)) {
            mapper.writeValue(fos, mapPoint);
            logger.info(String.format("saved map point {%s} in file {%s}", mapPoint, path));
        } catch (IOException e) {
            logger.error("An error occurred while saving map point: {}", e.getMessage());
            throw new DatabindException(
                    String.format("cannot write data to file {%s}", path));
        }
        logger.info("map point {} saved in file {}", mapPoint, path);
    }

    @Override
    public MapPoint load(String path, ObjectMapper mapper) throws NotFoundException, DatabindException {
        logger.info("loading data from file {}", path);


        if (Files.exists(Path.of(path))) {
            logger.error("file {} not exists", path);
            throw new NotFoundException("file", path);
        }

        MapPoint mapPoint;
        try (var fin = new FileInputStream(path)) {
            mapPoint = mapper.readValue(fin, MapPoint.class);
            logger.info("Loaded map point {} from file {}", mapPoint, path);
        } catch (IOException e) {
            logger.error("An error occurred while loading map point: {}", e.getMessage());
            throw new DatabindException(
                    String.format("unable to read file {%s} invalid content", path));
        }
        logger.info("loaded map point {} from file {}", mapPoint, path);
        return mapPoint;
    }
}
