package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.infrastructure.ICurrencyClient
import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.core.application.models.currency.ConvertCurrencyCommand
import com.akkarimzai.task5.core.application.models.event.ListEvents
import com.akkarimzai.task5.core.application.profiles.toEntity
import com.akkarimzai.task5.core.domain.entities.Event
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class EventService(
    private val currencyClient: ICurrencyClient,
    private val newsClient: INewsClient
) {
    private val logger = KotlinLogging.logger {}

    fun list(query: ListEvents): CompletableFuture<List<Event>> {
        logger.info { "Loading events $query" }
        val validationResult: List<String> = query.validate()
        if (validationResult.isNotEmpty()) {
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }
        }

        if (query.dateFrom > query.dateTo) {
            throw BadRequestException("Invalid date range!")
        }

        val budgetFuture = if (query.currency.uppercase() != "RUB") {
            CompletableFuture.supplyAsync {
                currencyClient.convertCurrency(
                    ConvertCurrencyCommand(query.currency, "RUB", query.budget)
                ).convertedAmount
            }
        } else {
            CompletableFuture.completedFuture(query.budget);
        }

        val eventFuture = CompletableFuture.supplyAsync {
            newsClient.fetchEvents(query.dateFrom, query.dateTo)
                .map { it.toEntity() }
        }

        return budgetFuture.thenCombine(eventFuture) { budget, events ->
            events.filter { it.dates.start in query.dateFrom..query.dateTo && it.price <= budget }
        }
    }
}