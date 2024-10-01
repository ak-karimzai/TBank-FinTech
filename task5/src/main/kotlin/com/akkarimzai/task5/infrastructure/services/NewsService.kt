package com.akkarimzai.task5.infrastructure.services

import com.akkarimzai.task5.core.application.exceptions.ServiceUnavailableException
import com.akkarimzai.task5.infrastructure.models.ApiCategory
import com.akkarimzai.task5.infrastructure.models.ApiLocation
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Service
class NewsService(private val client: RestClient) : INewsService {
    private val logger = KotlinLogging.logger {}

    override suspend fun fetchCategories(endpoint: String): List<ApiCategory> {
        logger.info { "Fetching categories" }

        val response = try {
            client.get()
                .uri(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<List<ApiCategory>>()
        } catch (e: Exception) {
            logger.debug { "Error while fetching entities from $endpoint: $e" }
            throw ServiceUnavailableException("An error occurred while fetching from $endpoint")
        }

        if (response.statusCode != HttpStatus.OK) {
            logger.error("An error occurred while fetching categories: {}", response)
            throw ServiceUnavailableException("Unable to fetch news categories")
        }
        return response.body ?: listOf()
    }

    override suspend fun fetchLocations(endpoint: String): List<ApiLocation> {
        logger.info { "Fetching locations" }

        val response = try {
            client.get()
                .uri(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<List<ApiLocation>>()
        } catch (e: Exception) {
            logger.debug { "Error while fetching entities from $endpoint: $e" }
            throw ServiceUnavailableException("An error occurred while fetching from $endpoint")
        }

        if (response.statusCode != HttpStatus.OK) {
            logger.error("An error occurred while fetching locations: {}", response)
            throw ServiceUnavailableException("Unable to fetch news locations")
        }
        return response.body ?: listOf()
    }
}