package com.akkarimzai.task5.api.controllers.v1

import com.akkarimzai.task5.core.application.models.event.ListEvents
import com.akkarimzai.task5.core.application.services.EventService
import com.akkarimzai.task5.core.domain.entities.Event
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.temporal.ChronoField

@RestController
@RequestMapping(value = ["/api/v1/events"])
@LogExecutionTime
class EventController(private val service: EventService) {
    @GetMapping
    fun list(@RequestParam(required = true) budget: Double,
             @RequestParam(required = true) currency: String,
             @RequestParam dateFrom: LocalDate = LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1),
             @RequestParam dateTo: LocalDate = LocalDate.now().with(ChronoField.DAY_OF_WEEK, 7)) : List<Event> {
        return service.list(ListEvents(
            budget = budget,
            currency = currency,
            dateFrom = dateFrom,
            dateTo = dateTo
        )).get()
    }
}