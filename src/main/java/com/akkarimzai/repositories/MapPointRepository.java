package com.akkarimzai.repositories;

import com.akkarimzai.exceptions.DatabindException;
import com.akkarimzai.exceptions.NotFoundException;
import com.akkarimzai.entities.MapPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface MapPointRepository {
    void save(MapPoint mapPoint, String path, ObjectMapper mapper) throws DatabindException;
    MapPoint load(String path, ObjectMapper mapper) throws NotFoundException, DatabindException;
}
