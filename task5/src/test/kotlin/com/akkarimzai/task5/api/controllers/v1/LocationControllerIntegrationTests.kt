package com.akkarimzai.task5.api.controllers.v1

import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ILocationRepository
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.application.models.location.UpdateLocation
import com.akkarimzai.task5.core.domain.entities.Location
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationControllerIntegrationTests(
    private val webTestClient: WebTestClient,
    private val repository: ILocationRepository
) : FunSpec({
    val URI = "/api/v1/locations"

    test("Should create a new location") {
        // Arrange
        val request = CreateLocation("test", "test");

        // Act && Assert
        webTestClient.post()
            .uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CreateLocation::class.java)
            .exchange()
            .expectStatus().isCreated
    }

    test("Incorrect create request should fail and have bad request status") {
        // Arrange
        val request = CreateLocation("test", "");

        // Act && Assert
        webTestClient.post()
            .uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CreateLocation::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Get Should return a single location") {
        // Arrange
        val entity = Location(UUID.randomUUID(), "test", "test")
        repository.save(entity)

        // Act
        val responseLocation = webTestClient.get()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Location::class.java)
            .returnResult()
            .responseBody

        // Assert
        responseLocation shouldNotBe null
        responseLocation?.shouldBeEqual(entity)
    }


    test("Get Should return not found (404) when location does not exist") {
        // Arrange
        val id = UUID.randomUUID()

        // Act && Assert
        webTestClient.get()
            .uri("$URI/$id")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    test("Get Should return bad request (400) when location id is incorrect") {
        // Arrange
        val id = "random-id"

        // Act && Assert
        webTestClient.get()
            .uri("$URI/$id")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Update should update an existing location") {
        // Arrange
        val entity = Location(UUID.randomUUID(), "test", "test")
        repository.save(entity)
        val request = UpdateLocation(null, "new test")

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateLocation::class.java)
            .exchange()
            .expectStatus().isNoContent

        val updatedLocation = webTestClient.get()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Location::class.java)
            .returnResult()
            .responseBody

        updatedLocation shouldNotBe null
        updatedLocation?.name.shouldBe(request.name)
    }

    test("Update should return not found when location does not exist") {
        // Arrange
        val entity = Location(UUID.randomUUID(), "test", "test")
        val request = UpdateLocation(null, "new test")

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateLocation::class.java)
            .exchange()
            .expectStatus().isNotFound
    }

    test("Update should return bad request when nothing to update") {
        // Arrange
        val entity = Location(UUID.randomUUID(), "test", "test")
        repository.save(entity)
        val request = UpdateLocation(null, null)

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateLocation::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Delete should delete location") {
        // Arrange
        val entity = Location(UUID.randomUUID(), "test", "test")
        repository.save(entity)

        // Act && Assert
        webTestClient.delete()
            .uri("$URI/${entity.id}")
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get()
            .uri("$URI/${entity.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    test("List should return a list of locations") {
        // Arrange
        val request = PageableList(1, 10)

        // Act && Assert
        webTestClient.get()
            .uri("$URI?page=${request.page}&size=${request.size}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(PaginatedList::class.java)
            .consumeWith { response ->
                val paginatedList = response.responseBody as PaginatedList<Location>
                paginatedList.page shouldBe 1
            }
    }

    test("List should return bad request when request params are not valid") {
        // Arrange
        val request = PageableList(0, 10)

        // Act && Assert
        webTestClient.get()
            .uri("$URI?page=${request.page}&size=${request.size}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }
})
