package com.akkarimzai.task10.services

import com.akkarimzai.task10.entities.Place
import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.exceptions.NotFoundException
import com.akkarimzai.task10.exceptions.ValidationException
import com.akkarimzai.task10.models.common.ValidatableCQ
import com.akkarimzai.task10.models.place.*
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.profiles.toPlaceDto
import com.akkarimzai.task10.profiles.toPlaceEntity
import com.akkarimzai.task10.repositories.PlaceRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val repository: PlaceRepository
) : AbstractService() {
    private val logger = KotlinLogging.logger {}

    fun create(command: CreatePlaceCommand): Long? {
        logger.info { "Request create: $command" }

        validateRequest(command)

        val place = command.toPlaceEntity()

        logger.info { "Mapped command: $place" }
        val savedPlace = repository.save(place)

        return savedPlace.id.also {
            logger.info { "Successfully created place: $savedPlace" }
        }
    }

    fun update(placeId: Long, command: UpdatePlaceCommand) {
        logger.info { "Request update: $command" }

        if (placeId <= 0) {
            throw BadRequestException("PlaceId must be greater than 0")
        }

        validateRequest(command)

        val place = loadPlace(placeId)
        val placeToUpdate = command.toPlaceEntity(place)

        logger.info { "Mapped update command: $placeToUpdate" }
        repository.save(placeToUpdate).also {
            logger.info { "Successfully updated: $it" }
        }
    }

    fun delete(command: DeletePlaceCommand) {
        logger.info { "Request delete: $command" }

        validateRequest(command)

        val place = loadPlace(command.placeId)
        repository.delete(place).also {
            logger.info { "Successfully deleted: $place" }
        }
    }

    fun get(query: GetPlaceQuery): PlaceDto {
        logger.info { "Request get: $query" }

        validateRequest(query)

        return loadPlace(query.placeId)
            .toPlaceDto()
    }

    fun list(query: ListPlaceQuery): Page<PlaceDto> {
        logger.info { "Request list: $query" }

        validateRequest(query)

        return repository.findAll(PageRequest.of(query.page, query.size))
            .map { it.toPlaceDto() }.also {
                logger.info { "Listed event list successfully." }
            }
    }

    private fun loadPlace(placeId: Long): Place {
        logger.info { "Loading place with id: $placeId" }

        return repository.findById(placeId)
            .orElseThrow {
                logger.debug { "Place with id $placeId not found." }
                NotFoundException("Place with id", placeId)
            }.also {
                logger.info { "loaded place: $it" }
            }
    }
}