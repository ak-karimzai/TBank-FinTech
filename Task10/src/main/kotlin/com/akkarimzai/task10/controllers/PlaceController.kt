package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.entities.Place
import com.akkarimzai.task10.models.event.EventDto
import com.akkarimzai.task10.models.place.*
import com.akkarimzai.task10.services.PlaceService
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/places"])
class PlaceController(
    private val service: PlaceService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun list(@RequestParam page: Int = 0,
             @RequestParam size: Int = 10,
             assembler: PagedResourcesAssembler<PlaceDto>
    ): PagedModel<EntityModel<PlaceDto>> {
        logger.info { "Request list" }
        val pagedEvents =  service.list(
            ListPlaceQuery(page = page, size = size))
        return assembler.toModel(pagedEvents)
    }

    @GetMapping("{placeId}")
    fun get(@PathVariable placeId: Long): PlaceDto {
        logger.info { "Request get" }

        return service.get(
            GetPlaceQuery(
                placeId = placeId
            )
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody command: CreatePlaceCommand): Long? {
        logger.info { "Request create" }

        return service.create(command)
    }

    @PutMapping("{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable placeId: Long,
               @RequestBody command: UpdatePlaceCommand) {
        logger.info { "Request update" }
        return service.update(placeId, command)
    }

    @DeleteMapping("{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable placeId: Long) {
        logger.info { "Request delete" }

        return service.delete(
            DeletePlaceCommand(placeId)
        )
    }
}