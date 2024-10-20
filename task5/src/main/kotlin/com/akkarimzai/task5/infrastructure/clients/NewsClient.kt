package com.akkarimzai.task5.infrastructure.clients

import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.application.exceptions.ServiceUnavailableException
import com.akkarimzai.task5.core.application.models.news.ApiCategory
import com.akkarimzai.task5.core.application.models.news.ApiEvent
import com.akkarimzai.task5.core.application.models.news.ApiLocation
import com.akkarimzai.task5.infrastructure.responses.EventResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.time.LocalDate
import java.time.ZoneOffset

@Service
class NewsClient(
    @Qualifier("news-client") private val client: RestClient,
    @Value("\${api.kudago.categories}") private val categories: String,
    @Value("\${api.kudago.locations}") private val locations: String,
    @Value("\${api.kudago.events}") private val events: String
) : INewsClient {
    private val logger = KotlinLogging.logger {}

    @CircuitBreaker(name = CIRCUIT_BREAKER_KEY, fallbackMethod = "onServiceFailure")
    override fun fetchCategories(): List<ApiCategory> {
        logger.info { "Fetching categories" }

        val response = client.get()
                .uri(categories)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<List<ApiCategory>>()

        if (response.statusCode != HttpStatus.OK) {
            onServiceFailure()
        }
        return response.body ?: listOf()
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_KEY, fallbackMethod = "onServiceFailure")
    override fun fetchLocations(): List<ApiLocation> {
        logger.info { "Fetching locations" }

        val response = client.get()
                .uri(locations)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<List<ApiLocation>>()

        if (response.statusCode != HttpStatus.OK) {
            onServiceFailure()
        }
        return response.body ?: listOf()
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_KEY, fallbackMethod = "onServiceFailure")
    override fun fetchEvents(dateFrom: LocalDate, dateTo: LocalDate): List<ApiEvent> {
        logger.info { "Fetching events" }

        val response = client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path(events)
                    .queryParam("actual_since", localDateToEpochMillis(dateFrom))
                    .queryParam("actual_until", localDateToEpochMillis(dateTo))
                    .queryParam("fields", "id,dates,title,price")
                    .queryParam("size", 100)
                    .build()
            }.accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity<EventResponse>()

        if (response.statusCode != HttpStatus.OK && response.body == null) {
            onServiceFailure()
        }
        return response.body?.results!!
    }

    private fun localDateToEpochMillis(localDate: LocalDate): Long {
        return localDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
    }

    fun onServiceFailure(ex: Throwable? = null): List<ApiLocation> {
        logger.error { "An error occurred while trying to fetch data from kudago: ${ex?.message}." }
        throw ServiceUnavailableException("News service not available.")
    }

    companion object {
        private const val CIRCUIT_BREAKER_KEY = "News"
    }
}