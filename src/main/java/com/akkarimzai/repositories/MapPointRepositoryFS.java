package com.akkarimzai.repositories;

import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.akkarimzai.entities.MapPoint;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Log4j2
public class MapPointRepositoryFS implements MapPointRepository {
    @Override
    public void save(MapPoint mapPoint, String path, ObjectMapper mapper) throws DatabindException {
        log.info("saving map point {} to file {}", mapPoint, path);

        try (var fos = new FileOutputStream(path)) {
            mapper.writeValue(fos, mapPoint);
            log.info(String.format("saved map point {%s} in file {%s}", mapPoint, path));
        } catch (IOException e) {
            log.error("An error occurred while saving map point: {}", e.getMessage());
            throw new DatabindException(
                    String.format("cannot write data to file {%s}", path));
        }
        log.info("map point {} saved in file {}", mapPoint, path);
    }

    @Override
    public MapPoint load(String path, ObjectMapper mapper) throws NotFoundException, DatabindException {
        log.info("loading data from file {}", path);


        if (Files.exists(Path.of(path))) {
            log.error("file {} not exists", path);
            throw new NotFoundException("file", path);
        }

        MapPoint mapPoint;
        try (var fin = new FileInputStream(path)) {
            mapPoint = mapper.readValue(fin, MapPoint.class);
            log.info("Loaded map point {} from file {}", mapPoint, path);
        } catch (IOException e) {
            log.error("An error occurred while loading map point: {}", e.getMessage());
            throw new DatabindException(
                    String.format("unable to read file {%s} invalid content", path));
        }
        log.info("loaded map point {} from file {}", mapPoint, path);
        return mapPoint;
    }
}
