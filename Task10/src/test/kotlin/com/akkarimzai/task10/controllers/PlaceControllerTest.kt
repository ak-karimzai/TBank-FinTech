package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.models.place.CreatePlaceCommand
import com.akkarimzai.task10.models.place.PlaceDto
import com.akkarimzai.task10.models.place.UpdatePlaceCommand
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PlaceControllerTest(
    @Autowired private val webTestClient: WebTestClient,
) : AbstractIntegrationTestConfig(webTestClient) {
    @Test
    fun `Should create place successfully`() {
        // Arrange
        var command = CreatePlaceCommand("Test", "Test", "Test", null)

        // Act && Assert
        createPlace(command)
    }

    @Test
    fun `create should through bad request when request body is not valid`() {
        // Arrange
        var command = CreatePlaceCommand("Test", "Test", "Test", "")

        // Act && Assert
        webTestClient.post()
            .uri("/api/places")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `get should fetch created place`() {
        // Arrange
        var command = CreatePlaceCommand("Test", "Test", "Test", null)

        // Act && Assert
        val createdPlaceId = webTestClient.post()
            .uri("/api/places")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody
        createdPlaceId!! shouldBeGreaterThan 0

        val createdPlace = fetchById(createdPlaceId)
        createdPlace shouldNotBe null
        createdPlace!!.id shouldBeEqual createdPlaceId
        createdPlace.name shouldBeEqual command.name
        createdPlace.address shouldBeEqual command.address
        createdPlace.description shouldBeEqual command.description
    }

    @Test
    fun `update should update required field ignore others`() {
        // Arrange
        val createCommand = CreatePlaceCommand("Test", "Test", "Test", null)
        val updateCommand = UpdatePlaceCommand("NewTest", null, null, null)

        // Act
        val createdPlace = createPlace(createCommand)
        webTestClient.put()
            .uri("/api/places/${createdPlace.id}")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isNoContent

        // Assert
        val updatedPlace = fetchById(createdPlace.id)
        updatedPlace shouldNotBe null
        updatedPlace!!.name shouldBeEqual updateCommand.name!!
    }

    @Test
    fun `update should return status 400 when nothing to update`() {
        // Arrange
        val createCommand = CreatePlaceCommand("Test", "Test", "Test", null)
        val updateCommand = UpdatePlaceCommand(null, null, null, null)

        // Act && Assert
        val createdPlace = createPlace(createCommand)
        webTestClient.put()
            .uri("/api/places/${createdPlace.id}")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommand)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `delete should remove entity from database`() {
        // Arrange
        val command = CreatePlaceCommand("Test", "Test", "Test", null)
        val createdPlace = createPlace(command)

        // Act && Assert
        webTestClient.delete()
            .uri("/api/places/${createdPlace.id}")
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("/api/places/${createdPlace.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `delete status should 400 when id is incorrect`() {
        // Arrange
        val placeId = -10L

        // Act && Assert
        webTestClient.delete()
            .uri("/api/places/$placeId")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `list should return place paged place list`() {
        // Arrange
        val page = 1
        val size = 10

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places")
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
        val page = -10
        val size = 10

        // Act && Arrange
        webTestClient.get()
            .uri { builder ->
                builder
                    .path("/api/places")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus().isBadRequest
    }
}
