package com.akkarimzai.task7.controllers

import com.akkarimzai.task7.clients.AbstractCurrencyClientFailureTest
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class CurrencyControllerFailureTests @Autowired constructor(
    private val webTestClient: WebTestClient,
) : AbstractCurrencyClientFailureTest() {
    val URI = "/currencies"

    @Test
    fun `rate should return service unavailable failure`() {
        // Arrange
        val currency = "USD"

        // Act && Assert
        webTestClient.get()
            .uri("$URI/rates/$currency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError
            .expectHeader().exists("Retry-After")
            .expectHeader().valueEquals("Retry-After", "3600")
    }

    @Test
    fun `convert should return service unavailable failure`() {
        // Arrange
        val request = ConvertCurrencyCommand(
            fromCurrency = "USD",
            toCurrency = "RUB",
            amount = 1.0
        )

        // Act && Assert
        webTestClient.post()
            .uri("/currencies/convert")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError
            .expectHeader().exists("Retry-After")
            .expectHeader().valueEquals("Retry-After", "3600")
    }
}