package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.models.event.CreateEventCommand
import com.akkarimzai.task10.models.event.EventDto
import com.akkarimzai.task10.models.event.UpdateEventCommand
import com.akkarimzai.task10.models.place.CreatePlaceCommand
import com.akkarimzai.task10.models.place.PlaceDto
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
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
) : AbstractIntegrationTestConfig(webTestClient) {
    @Test
    fun `create should create a new event`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val place = createPlace()

        // Act && Assert
        val createdEvent = createEvent(place.id, command)
        createdEvent.name shouldBeEqual command.name
        createdEvent.date shouldBeEqual command.date
        createdEvent.tagline?.shouldBeEqual(command.tagline!!)
    }

    @Test
    fun `create on invalid command should return 400 status`() {
        // Arrange
        val command = CreateEventCommand("", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val place = createPlace()

        // Act && Assert
        webTestClient.post()
            .uri("/api/places/${place.id}/events")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return valid event`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)

        // Act && Assert
        val createdEvent = createEvent(place.id, command)
        createdEvent.id shouldNotBe 0
        createdEvent.name shouldBeEqual place.name
        createdEvent.date shouldBeEqual command.date
        createdEvent.tagline?.shouldBeEqual(command.tagline!!)
    }

    @Test
    fun `get should return bad request in invalid id`() {
        // Arrange
        val place = createPlace()
        val invalidId = -10L

        // Act && Assert
        webTestClient.get()
            .uri("/api/places/${place.id}/events/$invalidId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return not found on non existing place`() {
        // Arrange
        val place = createPlace()
        val validId = 10L

        // Act && Assert
        webTestClient.get()
            .uri("/api/places/${place.id + 1000}/events/$validId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `update should update the field only`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val place = createPlace()
        val event = createEvent(place.id, command)
        val updateCommand = UpdateEventCommand("updatedtest", null, null)

        // Act && Assert
        webTestClient.put()
            .uri("/api/places/${place.id + 1000}/events/${event.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent
        val updatedEvent = getEvent(place.id, eventId = event.id)
        updatedEvent shouldNotBe null
        updatedEvent.name shouldBe updatedEvent.name
    }

    @Test
    fun `update should return bad request in invalid command`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val place = createPlace()
        val event = createEvent(place.id, command)
        val updateCommand = UpdateEventCommand("", null, null)

        // Act && Assert
        webTestClient.put()
            .uri("/api/places/${place.id + 1000}/events/${event.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `delete should remove an event`() {
        // Arrange
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val place = createPlace()
        val event = createEvent(place.id, command)

        // Act && Assert
        webTestClient.delete()
            .uri("/api/places/${place.id}/events/${event.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/api/places/${place.id + 1000}/events/${event.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `list should return event paged list`() {
        // Arrange
        val place = createPlace()
        val page = 1
        val size = 10

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places/${place.id}/events")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
    }

    @Test
    fun `list should return bad request on invalid page size`() {
        // Arrange
        val place = createPlace()
        val page = 1
        val size = 0

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places/${place.id}/events")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest
    }

    fun createEvent(placeId: Long, command: CreateEventCommand): EventDto {
        val createdEventId = webTestClient.post()
            .uri("/api/places/${placeId}/events")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody
        return getEvent(placeId, createdEventId!!)
    }

    fun getEvent(placeId: Long, eventId: Long): EventDto {
        return webTestClient.get()
            .uri("/api/places/${placeId}/events/$eventId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(EventDto::class.java)
            .returnResult()
            .responseBody!!
    }

    fun createPlace(): PlaceDto {
        return createPlace(
            CreatePlaceCommand(
                name = "test", address = "test", description = "test", subway = null
            ))
    }
}
