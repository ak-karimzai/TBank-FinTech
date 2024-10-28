package com.akkarimzai.task10.controllers.e2e

import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class AbstractIntegrationTestConfig {
    companion object {
        val psqlContainer = PostgreSQLContainer("postgres:14-alpine3.20").apply {
            withDatabaseName("task10")
            withUsername("root")
            withPassword("root")
        }

        @BeforeAll
        @JvmStatic
        fun startContainer() {
            psqlContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", psqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", psqlContainer::getUsername)
            registry.add("spring.datasource.password", psqlContainer::getPassword)
        }
    }
}