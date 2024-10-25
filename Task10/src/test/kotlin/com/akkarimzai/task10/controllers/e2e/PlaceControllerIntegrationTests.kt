package com.akkarimzai.task10.controllers.e2e

import com.akkarimzai.task10.models.place.CreatePlaceCommand
import com.akkarimzai.task10.models.place.PlaceDto
import com.akkarimzai.task10.models.place.UpdatePlaceCommand
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlaceControllerIntegrationTests(
    @Autowired private val webTestClient: WebTestClient
): AbstractIntegrationTestConfig() {
    @Test
    fun `should create place successfully and persist in DB`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", null)

        // Act
        val createdPlaceId = webTestClient.post()
            .uri("/api/places")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        // Assert
        val createdPlace = fetchById(createdPlaceId)
        assert(createdPlace != null)
        assert(createdPlace!!.name == command.name)
        assert(createdPlace.address == command.address)
        assert(createdPlace.description == command.description)
    }

    @Test
    fun `create should throw bad request when request body is not valid`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", "")

        // Act && Assert
        webTestClient.post()
            .uri("/api/places")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should fetch created place from DB`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", null)
        val createdPlaceId = createPlace(command)

        // Act
        val fetchedPlace = fetchById(createdPlaceId)

        // Assert
        assert(fetchedPlace != null)
        assert(fetchedPlace!!.id == createdPlaceId)
        assert(fetchedPlace.name == command.name)
    }

    @Test
    fun `update should update required field and ignore others in DB`() {
        // Arrange
        val createCommand = CreatePlaceCommand("Test", "Test", "Test", null)
        val updateCommand = UpdatePlaceCommand("NewTest", null, null, null)
        val createdPlaceId = createPlace(createCommand)

        // Act
        webTestClient.put()
            .uri("/api/places/${createdPlaceId}")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isNoContent

        // Assert
        val updatedPlace = fetchById(createdPlaceId)
        assert(updatedPlace != null)
        assert(updatedPlace!!.name == updateCommand.name!!)
    }

    @Test
    fun `delete should remove entity from database`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", null)
        val createdPlaceId = createPlace(command)
        val createdPlace = fetchById(createdPlaceId)

        // Act
        createdPlace shouldNotBe null
        webTestClient.delete()
            .uri("/api/places/${createdPlace!!.id}")
            .exchange()
            .expectStatus().isNoContent

        // Assert
        webTestClient.get()
            .uri("/api/places/${createdPlaceId}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `list should return place paged place list from DB`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", null)
        createPlace(command) // Ensure at least one place is created

        // Act && Assert
        webTestClient.get()
            .uri("/api/places?page=0&size=10")
            .exchange()
            .expectStatus().isOk
    }

    fun createPlace(command: CreatePlaceCommand): Long {
        return webTestClient.post()
            .uri("/api/places")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!
    }

    fun fetchById(id: Long): PlaceDto? {
        return webTestClient.get()
            .uri("/api/places/$id")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(PlaceDto::class.java)
            .returnResult()
            .responseBody
    }
}