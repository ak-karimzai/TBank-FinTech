package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.models.event.*
import com.akkarimzai.task10.services.EventService
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/places")
class EventController(
    private val service: EventService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("{placeId}/events")
    fun list(@PathVariable placeId: Long,
             @RequestParam page: Int = 1,
             @RequestParam size: Int = 10,
             @RequestParam name: String?,
             @RequestParam fromDate: LocalDateTime?,
             @RequestParam toDate: LocalDateTime?
    ): Page<EventDto> {
        logger.info { "Request list " }
        return service.list(
            placeId = placeId, query = ListEventsQuery(page, size, name, fromDate, toDate))
    }

    @GetMapping("{placeId}/events/{eventID}")
    fun get(@PathVariable placeId: Long, @PathVariable eventID: Long): EventDto {
        logger.info { "Request get " }

        return service.get(
            GetEventQuery(placeId, eventID)
        )
    }

    @PostMapping("{placeId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable placeId: Long,
               @RequestBody request: CreateEventCommand): Long? {
        logger.info { "Request get " }

        return service.create(placeId, request)
    }

    @PutMapping("{placeId}/events/{eventID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable placeId: Long,
               @PathVariable eventID: Long,
               @RequestBody request: UpdateEventCommand) {
        logger.info { "Request update " }
        return service.update(placeId, eventID, request)
    }

    @DeleteMapping("{placeId}/events/{eventID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable placeId: Long, @PathVariable eventID: Long) {
        logger.info { "Request delete " }
        service.delete(
            DeleteEventCommand(placeId, eventID)
        )
    }
}