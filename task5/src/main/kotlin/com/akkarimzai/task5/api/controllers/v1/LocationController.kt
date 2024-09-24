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
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{locationId}")
    fun get(@PathVariable locationId: UUID) : ResponseEntity<Location> {
        return ResponseEntity<Location>(service.load(id = locationId), HttpStatus.OK)
    }

    @GetMapping
    fun list(@ModelAttribute pageableList: PageableList) : ResponseEntity<PaginatedList<Location>> {
        return ResponseEntity<PaginatedList<Location>>(service.list(pageableList), HttpStatus.OK)
    }

    @PostMapping
    fun save(location: CreateLocation) : ResponseEntity<UUID> {
        return ResponseEntity<UUID>(service.save(location), HttpStatus.CREATED)
    }

    @PutMapping("/{locationId}")
    fun update(@PathVariable locationId: UUID, @RequestBody location: UpdateLocation) : ResponseEntity<Unit> {
        return ResponseEntity(service.update(locationId, location), HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{locationId}")
    fun delete(@PathVariable locationId: UUID) : ResponseEntity<Unit> {
        return ResponseEntity(service.delete(locationId), HttpStatus.NO_CONTENT)
    }
}