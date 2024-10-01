package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.infrastructure.services.INewsService
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class InMemoryDbContext(private val newsService: INewsService) {
    private val logger = KotlinLogging.logger {}

    @Value("\${api.kudago.categories}")
    private lateinit var categoriesUri: String

    @Value("\${api.kudago.locations}")
    private lateinit var locationsUri: String

    @Bean
    suspend fun categoryInMemoryStore(): InMemoryStore<UUID, Category> {
        val result = InMemoryStore<UUID, Category>()

        val categories = newsService.fetchCategories(categoriesUri)
        categories.let { it ->
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
    suspend fun locationInMemoryStore(): InMemoryStore<UUID, Location> {
        val result = InMemoryStore<UUID, Location>()

        val locations = newsService.fetchLocations(locationsUri)

        locations.let { it ->
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