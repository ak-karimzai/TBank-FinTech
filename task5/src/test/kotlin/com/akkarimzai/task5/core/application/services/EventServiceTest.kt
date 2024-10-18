package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.infrastructure.ICurrencyClient
import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.application.models.currency.ConvertCurrencyCommand
import com.akkarimzai.task5.core.application.models.currency.ConvertedCurrency
import com.akkarimzai.task5.core.application.models.event.ListEvents
import com.akkarimzai.task5.infrastructure.clients.mocks.NewsClientMock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import java.time.LocalDate
import java.time.temporal.ChronoField

class EventServiceTest(
    private val currencyClient: ICurrencyClient = mockk(),
    private val newsClient: INewsClient = NewsClientMock(),
    private val eventService: EventService = EventService(currencyClient, newsClient),
) : FunSpec({
    test("list return events list in budget and date range") {
        // Arrange
        val currency = "USD"
        val amount = 100.0
        val convertedAmount = amount * 97
        val query = ListEvents(amount, currency,
            LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1),
            LocalDate.now().with(ChronoField.DAY_OF_WEEK, 7))

        every { currencyClient.convertCurrency(ConvertCurrencyCommand(
            currency, "RUB", amount)) } returns ConvertedCurrency(currency, "RUB", convertedAmount)

        // Act
        val result = eventService.list(query)

        // Assert
        result.subscribe({ result ->
            result.size shouldNotBe 0
        },
        { error ->
            throw error
        })
    }

    test("list return empty list when budget tickets are not available for budget") {
        // Arrange
        val currency = "USD"
        val amount = 10.0
        val convertedAmount = amount * 97
        val query = ListEvents(amount, currency,
            LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1),
            LocalDate.now().with(ChronoField.DAY_OF_WEEK, 7))

        every { currencyClient.convertCurrency(ConvertCurrencyCommand(
            currency, "RUB", amount)) } returns ConvertedCurrency(currency, "RUB", convertedAmount)

        // Act
        val result = eventService.list(query)

        // Assert
        result.subscribe({ result ->
            result.size shouldBe  0
        },
        { error ->
            throw error
        })
    }
})
