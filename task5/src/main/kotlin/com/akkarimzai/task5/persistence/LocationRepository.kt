package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.application.contracts.ILocationRepository
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.responses.KudaGoResponse
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import java.util.*

@Repository
class LocationRepository : ILocationRepository {
    override fun save(entity: Location) {
        logger.info { "Saving location: $entity" }
        entity.id = UUID.randomUUID()
        locations.add(entity).also {
            logger.info { "Saved location: $entity" }
        }
    }

    override fun slugExist(slug: String): Boolean {
        logger.info { "Checking for slug existence: $slug" }
        return locations.any { it.slug.equals(slug) }.also {
            logger.info { "Found slug {$slug} existence: $it" }
        }
    }

    override fun load(id: UUID): Location? {
        logger.info { "Loading entity with id: $id" }
        return locations.firstOrNull { it.id == id }.also {
            logger.info { "Found entity with id: $it" }
        }
    }

    override fun update(entity: Location) {
        logger.info { "Updating Location with id: ${entity.id}" }
        locations.removeIf { it.id == entity.id }
        locations.add(entity).also {
            logger.info { "Updated Location: $entity" }
        }
    }

    override fun delete(id: UUID) {
        logger.info { "Deleting Location with: $id" }
        locations.removeIf { it.id == id }.also {
            logger.info { "Found and deleted Location with id {$id}: $it" }
        }
    }

    override fun count(): Int {
        return locations.size.also {
            logger.info { "Total location amount: $it" }
        }
    }

    override fun list(pageableList: PageableList): List<Location> {
        logger.info { "Loading location list: $pageableList" }
        return locations
            .drop((pageableList.page - 1) * pageableList.size)
            .take(pageableList.size).also {
                logger.info { "Loaded page: ${pageableList.page} with size: ${pageableList.size} locations" }
            }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        private val locations: MutableList<Location> by lazy {
            Collections.synchronizedList(mutableListOf<Location>()).apply {
                addAll(fetchLocationList(RestTemplate()))
            }
        }

        private fun fetchLocationList(restTemplate: RestTemplate): List<Location> {
            logger.info { "Fetching location list from kudago API" }
            val response = try {
                restTemplate.getForObject(
                    "https://kudago.com/public-api/v1.4/locations",
                    Array<KudaGoResponse>::class.java).also {
                        logger.info { "Loaded ${it?.size ?: 0} locations" }
                }
            } catch (e: Exception) {
                logger.debug { "Can't fetch locations from kudago API: $e" }
                null
            }
            return response?.map { Location(UUID.randomUUID(), it.slug, it.name) } ?: emptyList()
        }
    }
}