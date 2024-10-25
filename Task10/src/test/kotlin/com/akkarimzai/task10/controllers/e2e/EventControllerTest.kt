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
) {
    @Test
    fun `create should create a new event`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)

        // Act
        val createdEventId = webTestClient.post()
            .uri("/api/places/${place.id}/events")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        // Assert
        val createdEvent = getEvent(place.id, createdEventId)
        assert(createdEvent.name == command.name)
        assert(createdEvent.date == command.date)
        assert(createdEvent.tagline == command.tagline)
    }

    @Test
    fun `create on invalid command should return 400 status`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("", LocalDateTime.of(2020, 1, 1, 10, 10), null)

        // Act & Assert
        webTestClient.post()
            .uri("/api/places/${place.id}/events")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return valid event`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val createdEventId = createEvent(place.id, command)

        // Act
        val fetchedEvent = getEvent(place.id, createdEventId)

        // Assert
        assert(fetchedEvent != null)
        assert(fetchedEvent.name == command.name)
        assert(fetchedEvent.date == command.date)
    }

    @Test
    fun `get should return bad request for invalid id`() {
        // Arrange
        val place = createPlace()
        val invalidId = -10L

        // Act & Assert
        webTestClient.get()
            .uri("/api/places/${place.id}/events/$invalidId")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should return not found on non-existing place`() {
        // Arrange
        val placeId = 999L // Assuming this ID doesn't exist
        val validEventId = 10L // Assuming this ID doesn't exist

        // Act & Assert
        webTestClient.get()
            .uri("/api/places/$placeId/events/$validEventId")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `update should update the event field only`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val eventId = createEvent(place.id, command)
        val updateCommand = UpdateEventCommand("updatedtest", null, null)

        // Act
        webTestClient.put()
            .uri("/api/places/${place.id}/events/$eventId")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isNoContent

        // Assert
        val updatedEvent = getEvent(place.id, eventId)
        assert(updatedEvent.name == updateCommand.name)
    }

    @Test
    fun `update should return bad request for invalid command`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val eventId = createEvent(place.id, command)
        val updateCommand = UpdateEventCommand("", null, null)

        // Act & Assert
        webTestClient.put()
            .uri("/api/places/${place.id}/events/$eventId")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `delete should remove an event`() {
        // Arrange
        val place = createPlace()
        val command = CreateEventCommand("test", LocalDateTime.of(2020, 1, 1, 10, 10), null)
        val eventId = createEvent(place.id, command)

        // Act
        webTestClient.delete()
            .uri("/api/places/${place.id}/events/$eventId")
            .exchange()
            .expectStatus().isNoContent

        // Assert
        webTestClient.get()
            .uri("/api/places/${place.id}/events/$eventId")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `list should return event paged list`() {
        // Arrange
        val place = createPlace()
        createEvent(place.id, CreateEventCommand("event1", LocalDateTime.now(), null)) // Prepopulate

        // Act & Assert
        webTestClient.get()
            .uri("/api/places/${place.id}/events?page=0&size=10")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray // Ensure it's an array
    }

    @Test
    fun `list should return bad request on invalid page size`() {
        // Arrange
        val place = createPlace()

        // Act & Assert
        webTestClient.get()
            .uri("/api/places/${place.id}/events?page=0&size=0")
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

    private fun createPlace(): PlaceDto {
        return webTestClient.post()
            .uri("/api/places")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CreatePlaceCommand("test", "test address", "test description", null))
            .exchange()
            .expectStatus().isCreated
            .expectBody(PlaceDto::class.java)
            .returnResult()
            .responseBody!!
    }
}
