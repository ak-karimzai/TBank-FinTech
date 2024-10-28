package com.akkarimzai.task10.controllers.e2e

import com.akkarimzai.task10.models.event.CreateEventCommand
import com.akkarimzai.task10.models.event.EventDto
import com.akkarimzai.task10.models.event.UpdateEventCommand
import com.akkarimzai.task10.models.place.CreatePlaceCommand
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest(
    @Autowired private val webTestClient: WebTestClient,
): AbstractIntegrationTestConfig() {
    @Test
    fun `create should create a new event`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val placeId = createPlace()

        // Act && Assert
        val createdEventId = createEvent(placeId, command)
        val createdEvent = getEvent(placeId, createdEventId)
        createdEvent.name shouldBeEqual command.name
        createdEvent.date shouldBeEqual command.date
        createdEvent.tagline?.shouldBeEqual(command.tagline!!)
    }

    @Test
    fun `create on invalid command should return 400 status`() {
        // Arrange
        val command = CreateEventCommand("", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val placeId = createPlace()

        // Act && Assert
        webTestClient.post()
            .uri("/api/places/${placeId}/events")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return valid event`() {
        // Arrange
        val placeId = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)

        // Act && Assert
        val createdEventId = createEvent(placeId, command)
        val createdEvent = getEvent(placeId, createdEventId)
        createdEvent.id shouldNotBe 0
        createdEvent.name shouldBeEqual command.name
        createdEvent.date shouldBeEqual command.date
        createdEvent.tagline?.shouldBeEqual(command.tagline!!)
    }

    @Test
    fun `get should return bad request in invalid id`() {
        // Arrange
        val placeId = createPlace()
        val invalidId = -10L

        // Act && Assert
        webTestClient.get()
            .uri("/api/places/${placeId}/events/$invalidId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return not found on non existing place`() {
        // Arrange
        val placeId = 99999L
        val validId = 10L

        // Act && Assert
        webTestClient.get()
            .uri("/api/places/${placeId}/events/$validId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `update should update the field only`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val placeID = createPlace()
        val eventID = createEvent(placeID, command)
        val updateCommand = UpdateEventCommand("updatedtest", null, null)

        // Act && Assert
        webTestClient.put()
            .uri("/api/places/${placeID}/events/${eventID}")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isNoContent
        val updatedEvent = getEvent(placeID, eventID)
        updatedEvent shouldNotBe null
        updatedEvent.name shouldBeEqual updatedEvent.name
    }

    @Test
    fun `update should return bad request in invalid command`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val placeId = createPlace()
        val eventId = createEvent(placeId, command)
        val updateCommand = UpdateEventCommand("", null, null)

        // Act && Assert
        webTestClient.put()
            .uri("/api/places/${placeId}/events/${eventId}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `delete should remove an event`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10))
        val placeId = createPlace()
        val eventId = createEvent(placeId, command)

        // Act && Assert
        webTestClient.delete()
            .uri("/api/places/${placeId}/events/${eventId}")
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/api/places/${placeId}/events/${eventId}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `list should return event paged list`() {
        // Arrange
        val placeId = createPlace()
        val page = 1
        val size = 10

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places/${placeId}/events")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `list should return bad request on invalid page size`() {
        // Arrange
        val placeId = createPlace()
        val page = 1
        val size = 0

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places/${placeId}/events")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest
    }

    private fun createEvent(placeId: Long, command: CreateEventCommand): Long {
        return webTestClient.post()
            .uri("/api/places/$placeId/events")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!
    }

    private fun getEvent(placeId: Long, eventId: Long): EventDto {
        return webTestClient.get()
            .uri("/api/places/$placeId/events/$eventId")
            .exchange()
            .expectStatus().isOk
            .expectBody(EventDto::class.java)
            .returnResult()
            .responseBody!!
    }

    private fun createPlace(): Long {
        return webTestClient.post()
            .uri("/api/places")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CreatePlaceCommand("test", "test address", "test description", null))
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!
    }
}