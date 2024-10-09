package com.akkarimzai.task7.clients

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.wiremock.integrations.testcontainers.WireMockContainer

@Testcontainers
abstract class AbstractCurrencyClientFailureTest {
    companion object {
        @Container
        @JvmStatic
        val wiremock: WireMockContainer =
            WireMockContainer("wiremock/wiremock:3.5.4")
                .withMappingFromResource("cbr-failure-stub", "cbr-failure-stub.json")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("api.cbr.base-url", wiremock::getBaseUrl)
        }
    }
}