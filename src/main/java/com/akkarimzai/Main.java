package com.akkarimzai;

import com.akkarimzai.controllers.MapPointController;
import com.akkarimzai.models.DeserializeFileDto;
import com.akkarimzai.repositories.MapPointRepository;
import com.akkarimzai.repositories.MapPointRepositoryFS;
import com.akkarimzai.services.MapPointService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
        ObjectMapper deserializer = new ObjectMapper();
        ObjectMapper serializer = new XmlMapper();

        MapPointService mapPointService = new MapPointService(mapPointRepository, deserializer, serializer);

        MapPointController mapPointController = new MapPointController(mapPointService);

        var srcPath = args[0];
        var dstPath = args[1];

        mapPointController.deserialize(
                new DeserializeFileDto(srcPath, dstPath));

        logger.info("App stopped successfully");
    }
}