package com.akkarimzai.task5.infrastructure.clients

import com.akkarimzai.task5.core.application.exceptions.ServiceUnavailableException
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.wiremock.integrations.testcontainers.WireMockContainer
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Testcontainers
@SpringBootTest
class NewsClientIntegrationTests {

    @Autowired
    private lateinit var newsService: NewsClient

    @Test
    fun `categories list are not empty`() {
        // Act
        val categories = newsService.fetchLocations()

        // Assert
        categories.size shouldNotBe 0
    }

    @Test
    fun `locations list are not empty`() {
        // Act
        val locations = newsService.fetchLocations()

        // Assert
        locations.size shouldNotBe 0
    }

    @Test
    fun `locations when uri is invalid should failed in request`(): Unit = runBlocking {
        // Act && Assert
        assertThrows<ServiceUnavailableException> {
            val locations = newsService.fetchLocations()
        }
    }

    @Test
    fun `categories when uri is invalid should failed in request`(): Unit = runBlocking {
        // Act && Assert
        assertThrows<ServiceUnavailableException> {
            val locations = newsService.fetchCategories()
        }
    }

    companion object {
        @Container
        @JvmStatic
        val wiremock: WireMockContainer =
            WireMockContainer("wiremock/wiremock:3.5.4")
                .withMappingFromResource("kudago-success-stub", "kudago-success-stub.json")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("api.kudago.base-url", wiremock::getBaseUrl)
        }
    }
}
