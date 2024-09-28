package com.akkarimzai.task5.api.controllers.v1

import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.application.models.location.UpdateLocation
import com.akkarimzai.task5.core.application.services.LocationService
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/v1/locations"])
@LogExecutionTime
class LocationController(private val service: LocationService) {
    @GetMapping("/{locationId}")
    fun get(@PathVariable locationId: UUID) : Location {
        return service.load(id = locationId)
    }

    @GetMapping
    fun list(@ModelAttribute pageableList: PageableList) : PaginatedList<Location> {
        return service.list(pageableList)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody location: CreateLocation) : UUID {
        return service.save(location)
    }

    @PutMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable locationId: UUID, @RequestBody location: UpdateLocation) {
        return service.update(locationId, location)
    }

    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable locationId: UUID) {
        return service.delete(locationId)
    }
}