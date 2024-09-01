package com.akkarimzai.Repositories;

import com.akkarimzai.Exceptions.DatabindException;
import com.akkarimzai.Exceptions.NotFoundException;
import com.akkarimzai.Entities.MapPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface MapPointRepository {
    void save(MapPoint mapPoint, String path, ObjectMapper mapper) throws DatabindException;
    MapPoint load(String path, ObjectMapper mapper) throws NotFoundException, DatabindException;
}
