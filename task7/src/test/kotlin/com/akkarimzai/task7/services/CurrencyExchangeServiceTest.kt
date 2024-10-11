package com.akkarimzai.task7.services

import com.akkarimzai.task7.clients.CbrCurrencyClientMock
import com.akkarimzai.task7.exceptions.BadRequestException
import com.akkarimzai.task7.exceptions.NotFoundException
import com.akkarimzai.task7.models.ConvertCurrencyCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class CurrencyExchangeServiceTest(
    private val service: CurrencyExchangeService = CurrencyExchangeService(CbrCurrencyClientMock()),
) : FunSpec({
    test("rate successfully return currency info") {
        // Arrange
        val currency = "USD"
        val rate = 94.0

        // Act
        val currencyInfo = service.rate(currency)

        // Assert
        currencyInfo.currency shouldBeEqual currency
        currencyInfo.rate shouldBeEqual rate
    }

    test("rate currency invalid throws bad exception") {
        // Arrange
        val currency = "AAA"
        val rate = 94.0

        // Act && Assert
        assertThrows<BadRequestException> {
            service.rate(currency)
        }
    }

    test("rate currency valid but not in currency list throws not found exception") {
        // Arrange
        val currency = "AZN"

        // Act && Assert
        assertThrows<NotFoundException> {
            service.rate(currency)
        }
    }

    test("convert 1 usd equal 94 rub") {
        // Arrange
        val request = ConvertCurrencyCommand(
            fromCurrency = "USD",
            toCurrency = "RUB",
            amount = 1.0
        )
        val convertedAmount = 94.0

        // Act
        val converted = service.convert(request)

        // Assert
        converted.toCurrency shouldBeEqual request.toCurrency
        converted.fromCurrency shouldBeEqual request.fromCurrency
        converted.convertedAmount shouldBeEqual convertedAmount
    }

    test("convert 94 rub should 1 usd") {
        // Arrange
        val request = ConvertCurrencyCommand(
            fromCurrency = "RUB",
            toCurrency = "USD",
            amount = 94.0
        )
        val convertedAmount = 1.0

        // Act
        val converted = service.convert(request)

        // Assert
        converted.toCurrency shouldBeEqual request.toCurrency
        converted.fromCurrency shouldBeEqual request.fromCurrency
        converted.convertedAmount shouldBeEqual convertedAmount
    }

    test("convert should throw bad request exception when both currencies are equal") {
        // Arrange
        val request = ConvertCurrencyCommand(
            fromCurrency = "USD",
            toCurrency = "USD",
            amount = 94.0
        )

        // Act && Assert
        shouldThrow<BadRequestException> {
            service.convert(request)
        }
    }
})
