package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.responses.KudaGoResponse
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.util.UUID

@Configuration
class InMemoryDbContext {
    private val logger = KotlinLogging.logger {}

    @Value("\${api.kudago.base-url}")
    private lateinit var baseUrl: String

    @Value("\${api.kudago.categories}")
    private lateinit var categoriesUri: String

    @Value("\${api.kudago.locations}")
    private lateinit var locationsUri: String

    private lateinit var client: RestClient

    @PostConstruct
    fun init() {
        client = RestClient.create(baseUrl)
    }

    @Bean
    fun categoryInMemoryStore(): InMemoryStore<UUID, Category> {
        val result = InMemoryStore<UUID, Category>()
        logger.info { "Fetching categories" }

        val response = try {
            client.get()
                .uri(categoriesUri)
                .retrieve()
                .toEntity<List<KudaGoResponse>>()
        } catch (e: Exception) {
            logger.error { "Can't fetch categories from kudago API: $e" }
            null
        }

        if (response!!.statusCode != HttpStatus.OK) {
            logger.error { "Failed to fetch categories from kudago API: $response" }
        }

        response.body?.let { it ->
            it.forEach { kudagoCategory ->
                val id = UUID.randomUUID()
                result.collection[id] = Category(
                    id = id,
                    slug = kudagoCategory.slug,
                    name = kudagoCategory.name
                )
            }
            logger.info { "${it.size} categories fetched successfully" }
        }

        return result
    }

    @Bean
    fun locationInMemoryStore(): InMemoryStore<UUID, Location> {
        val result = InMemoryStore<UUID, Location>()

        logger.info { "Fetching locations" }
        val response = try {
            client.get()
                .uri(locationsUri)
                .retrieve()
                .toEntity<List<KudaGoResponse>>()
        } catch (e: Exception) {
            logger.error { "Can't fetch locations from kudago API: $e" }
            null
        }

        if (response!!.statusCode != HttpStatus.OK) {
            logger.error { "Failed to fetch locations from kudago API: $response" }
        }

        response.body?.let { it ->
            it.forEach { kudagoLocation ->
                val id = UUID.randomUUID()
                result.collection[id] = Location(
                    id = id,
                    slug = kudagoLocation.slug,
                    name = kudagoLocation.name
                )
            }
            logger.info { "${it.size} locations fetched successfully" }
        }

        return result
    }
}