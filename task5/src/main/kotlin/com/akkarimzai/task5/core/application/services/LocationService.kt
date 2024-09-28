package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.ILocationRepository
import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import com.akkarimzai.task5.core.application.exceptions.NotFoundException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.application.models.location.UpdateLocation
import com.akkarimzai.task5.core.application.profiles.toEntity
import com.akkarimzai.task5.core.domain.entities.Location
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LocationService(private val repository: ILocationRepository) {
    private val logger = KotlinLogging.logger {}

    fun save(newLocation: CreateLocation) : UUID {
        logger.info { "Saving new location: $newLocation" }
        val validationResult: List<String> = newLocation.validate()
        if (validationResult.isNotEmpty()) {
            logger.error { "Validation failure, validation result: $validationResult" }
            throw ValidationException(validationResult)
        }

        if (repository.slugExist(newLocation.slug)) {
            logger.error { "Location already exists, creating new location failed" }
            throw BadRequestException("Location already exists")
        }

        logger.info { "Mapping $newLocation to location" }
        val entity = newLocation.toEntity()

        repository.save(entity)
        return entity.id.also {
            logger.info { "Location created: $it" }
        }
    }

    fun load(id: UUID) : Location {
        logger.info { "Loading location for id: $id" }
        return repository.load(id).also {
            logger.info { "Location loaded for id: $id" }
        } ?: throw NotFoundException("Location", id).also {
            logger.error { "Location: $id not found" }
        }
    }

    fun list(pageableList: PageableList) : PaginatedList<Location> {
        logger.info { "Loading location list: $pageableList" }
        val validationResult: List<String> = pageableList.validate()
        if (validationResult.isNotEmpty())
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }

        logger.info { "Loading list: $pageableList, from repository" }
        val list = repository.list(pageableList.page, pageableList.size)
        val count = repository.count()

        return PaginatedList(
            list,
            count,
            pageableList.page,
            pageableList.size
        ).also {
            logger.info { "Loaded page: ${it.page}, available pages: ${it.totalPages}" }
        }
    }

    fun update(id: UUID, updateLocation: UpdateLocation) {
        logger.info { "Updating location for id: $id, location: $updateLocation" }
        val validationResult: List<String> = updateLocation.validate()
        if (validationResult.isNotEmpty())
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }

        val locationToUpdate = this.load(id)

        updateLocation.slug?.let {
            val exist = repository.slugExist(it)
            if (exist) {
                throw BadRequestException("slug exist.")
            }
            locationToUpdate.slug = it
        }

        updateLocation.name?.let {
            locationToUpdate.name = it
        }

        logger.info { "Updating location: $locationToUpdate" }
        repository.update(locationToUpdate).also {
            logger.info { "Location updated: $locationToUpdate" }
        }
    }

    fun delete(id: UUID) {
        logger.info { "Deleting location for id: $id" }
        this.load(id)

        repository.delete(id).also {
            logger.info { "Location deleted for id: $id" }
        }
    }
}