package com.akkarimzai.task7.controllers

import com.akkarimzai.task7.clients.AbstractCurrencyClientSuccessTest
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import com.akkarimzai.task7.models.ConvertedCurrencyDto
import com.akkarimzai.task7.models.CurrencyInfoDto
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldNotBe
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
class CurrencyControllerSuccessTests @Autowired constructor(
    private val webTestClient: WebTestClient,
) : AbstractCurrencyClientSuccessTest() {
    val URI = "/currencies"

    @Test
    fun `rate should return the currency info`() {
        // Arrange
        val currency = "USD"

        // Act
        val currencyInfo = webTestClient.get()
            .uri("$URI/rates/$currency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(CurrencyInfoDto::class.java)
            .returnResult()
            .responseBody

        // Assert
        currencyInfo shouldNotBe null
        currencyInfo?.currency?.shouldBeEqual(currency)
    }

    @Test
    fun `rate should return bad request when currency is invalid`() {
        // Arrange
        val currency = "AAA"

        // Act && Assert
        webTestClient.get()
            .uri("$URI/rates/$currency")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `convert should convert correctly`() {
        // Arrange
        val request = ConvertCurrencyCommand(
            fromCurrency = "USD",
            toCurrency = "RUB",
            amount = 1.0
        )

        // Act && Assert
        val convertedCurrencyDto = webTestClient.post()
            .uri("/currencies/convert")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(ConvertedCurrencyDto::class.java)
            .returnResult()
            .responseBody

        convertedCurrencyDto shouldNotBe null
        convertedCurrencyDto?.fromCurrency?.shouldBeEqual(request.fromCurrency)
        convertedCurrencyDto?.toCurrency?.shouldBeEqual(request.toCurrency)
        convertedCurrencyDto?.convertedAmount?.shouldBeEqual(96.1079)
    }
}
