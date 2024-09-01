package com.akkarimzai;

import com.akkarimzai.Controllers.MapPointController;
import com.akkarimzai.Models.DeserializeFileDto;
import com.akkarimzai.Profiles.MapPointJsonMapper;
import com.akkarimzai.Profiles.MapPointXmlMapper;
import com.akkarimzai.Repositories.MapPointRepository;
import com.akkarimzai.Repositories.MapPointRepositoryFS;
import com.akkarimzai.Services.MapPointService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("""
                   -- MapPoint Json to XML Deserializer --
                usage: app.jar [src path] [dst path]
                """);
            System.exit(-1);
        }

        logger.info("App started");
        MapPointRepository mapPointRepository = new MapPointRepositoryFS();
        ObjectMapper deserializer = new MapPointJsonMapper();
        ObjectMapper serializer = new MapPointXmlMapper();

        MapPointService mapPointService = new MapPointService(mapPointRepository, deserializer, serializer);

        MapPointController mapPointController = new MapPointController(mapPointService);

        var srcPath = args[0];
        var dstPath = args[1];

        mapPointController.deserialize(
                new DeserializeFileDto(srcPath, dstPath));

        logger.info("App stopped successfully");
    }
}