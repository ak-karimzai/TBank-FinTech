package com.akkarimzai.task10.services

import com.akkarimzai.task10.entities.Event
import com.akkarimzai.task10.exceptions.BadRequestException
import com.akkarimzai.task10.exceptions.ForbiddenRequestException
import com.akkarimzai.task10.exceptions.NotFoundException
import com.akkarimzai.task10.exceptions.ValidationException
import com.akkarimzai.task10.models.common.ValidatableCQ
import com.akkarimzai.task10.models.event.*
import com.akkarimzai.task10.profiles.toEventDto
import com.akkarimzai.task10.profiles.toEventEntity
import com.akkarimzai.task10.repositories.EventRepository
import com.akkarimzai.task10.repositories.PlaceRepository
import com.akkarimzai.task10.repositories.specs.EventSpecifications
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val placeRepository: PlaceRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun create(placeId: Long, command: CreateEventCommand): Long? {
        logger.info { "Request create: $command" }

        if (placeId <= 0) {
            throw BadRequestException("PlaceId must be greater than 0")
        }

        validateRequest(command)

        val place = placeRepository.findById(placeId)
            .orElseThrow {
                NotFoundException("Place with id", placeId)
            }

        val event = command.toEventEntity(place)

        logger.info { "Mapped update command: $event" }
        val savedEvent = eventRepository.save(event)

        return savedEvent.id
    }

    fun update(placeId: Long, eventId: Long, command: UpdateEventCommand) {
        logger.info { "Request update: $command" }

        if (placeId <= 0) {
            throw BadRequestException("PlaceId must be greater than 0")
        }

        if (eventId <= 0) {
            throw BadRequestException("Event must be greater than 0")
        }

        validateRequest(command)

        val event = loadEvent(placeId, eventId)
        val eventToUpdate = command.toEventEntity(event)

        logger.info { "Mapped update command: $eventToUpdate" }
        eventRepository.save(eventToUpdate).also {
            logger.info { "Updated event: $it" }
        }
    }

    fun delete(command: DeleteEventCommand) {
        logger.info { "Request delete: $command" }

        validateRequest(command)

        val event = loadEvent(command.placeId, command.eventId)

        logger.info { "Mapped delete command: $event" }
        eventRepository.delete(event).also {
            logger.info { "Deleted event with id: ${command.eventId}" }
        }
    }

    fun get(query: GetEventQuery): EventDto {
        logger.info { "Request get: $query" }

        validateRequest(query)

        return loadEvent(query.placeId, query.eventId)
            .toEventDto()
    }

    fun list(placeId: Long, query: ListEventsQuery): Page<EventDto> {
        logger.info { "Request list: $query" }

        if (placeId <= 0) {
            throw BadRequestException("PlaceId must be greater than 0")
        }

        validateRequest(query)

        val existsPlaceById = placeRepository.existsById(placeId)
        if (!existsPlaceById) {
            throw NotFoundException("Place with id", placeId)
        }

        return eventRepository.findAll(
            EventSpecifications.buildSpecification(placeId, query.name, query.fromDate, query.toDate),
            PageRequest.of(query.page, query.size))
            .map { it.toEventDto() }
    }

    private fun validateRequest(request: ValidatableCQ) {
        val validationResult = request.validate()
        validationResult.let {
            if (it.isNotEmpty()) {
                throw ValidationException(it)
            }
        }
    }

    private fun loadEvent(placeId: Long, eventId: Long): Event {
        logger.info { "Loading event: $eventId" }
        val event = eventRepository.findById(eventId)
            .orElseThrow {
                logger.debug { "Event with id $eventId does not exist." }
                NotFoundException("Event with id", eventId)
            }

        if (event.place.id != placeId) {
            logger.debug { "Event with id $eventId does not depend on place with id: {$placeId}." }
            throw ForbiddenRequestException(
                "Event: {$eventId} is not depend on place: {$placeId}")
        }

        return event.also {
            logger.info { "Event loaded: $it" }
        }
    }
}