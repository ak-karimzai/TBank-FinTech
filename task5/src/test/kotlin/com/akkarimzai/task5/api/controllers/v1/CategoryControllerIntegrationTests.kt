package com.akkarimzai.task5.api.controllers.v1

import com.akkarimzai.task5.core.application.contracts.ICategoryRepository
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.category.UpdateCategory
import com.akkarimzai.task5.core.domain.entities.Category
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
class CategoryControllerIntegrationTests(
    private val webTestClient: WebTestClient,
    private val repository: ICategoryRepository
) : FunSpec({
    val URI = "/api/v1/categories"

    test("Should create a new category") {
        // Arrange
        val request = CreateCategory("test", "test");

        // Act && Assert
        webTestClient.post()
            .uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CreateCategory::class.java)
            .exchange()
            .expectStatus().isCreated
    }

    test("Incorrect create request should fail and have bad request status") {
        // Arrange
        val request = CreateCategory("test", "");

        // Act && Assert
        webTestClient.post()
            .uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CreateCategory::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Get Should return a single category") {
        // Arrange
        val entity = Category(UUID.randomUUID(), "test", "test")
        repository.save(entity)

        // Act
        val responseCategory = webTestClient.get()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Category::class.java)
            .returnResult()
            .responseBody

        // Assert
        responseCategory shouldNotBe null
        responseCategory?.shouldBeEqual(entity)
    }


    test("Get Should return not found (404) when category does not exist") {
        // Arrange
        val id = UUID.randomUUID()

        // Act && Assert
       webTestClient.get()
            .uri("$URI/$id")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    test("Get Should return bad request (400) when category id is incorrect") {
        // Arrange
        val id = "random-id"

        // Act && Assert
        webTestClient.get()
            .uri("$URI/$id")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Update should update an existing category") {
        // Arrange
        val entity = Category(UUID.randomUUID(), "test", "test")
        repository.save(entity)
        val request = UpdateCategory(null, "new test")

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateCategory::class.java)
            .exchange()
            .expectStatus().isNoContent

        val updatedCategory = webTestClient.get()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Category::class.java)
            .returnResult()
            .responseBody

        updatedCategory shouldNotBe null
        updatedCategory?.name.shouldBe(request.name)
    }

    test("Update should return not found when category does not exist") {
        // Arrange
        val entity = Category(UUID.randomUUID(), "test", "test")
        val request = UpdateCategory(null, "new test")

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateCategory::class.java)
            .exchange()
            .expectStatus().isNotFound
    }

    test("Update should return bad request when nothing to update") {
        // Arrange
        val entity = Category(UUID.randomUUID(), "test", "test")
        repository.save(entity)
        val request = UpdateCategory(null, null)

        // Act && Assert
        webTestClient.put()
            .uri("$URI/${entity.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), UpdateCategory::class.java)
            .exchange()
            .expectStatus().isBadRequest
    }

    test("Delete should delete category") {
        // Arrange
        val entity = Category(UUID.randomUUID(), "test", "test")
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

    test("List should return a list of categories") {
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
                val paginatedList = response.responseBody as PaginatedList<Category>
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