package com.akkarimzai.task10.controllers.e2e

import com.akkarimzai.task10.entities.Role
import com.akkarimzai.task10.entities.User
import com.akkarimzai.task10.models.user.JwtAuthResponse
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.repositories.UserRepository
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
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
        fun startContainer(): Unit {
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

    protected lateinit var adminToken: String
    protected lateinit var userToken: String

    protected fun createUsersSaveThemInDatabaseAndRetrieveToken(userRepository: UserRepository,
                                                                webTestClient: WebTestClient,
                                                                passwordEncoder: PasswordEncoder) {
        val admin = userRepository.findByUsername("adminadmin") ?: User(
            username = "adminadmin",
            password = passwordEncoder.encode("adminadmin"),
            email = "admin@test.com",
            role = Role.ADMIN
        ).also { userRepository.save(it) }

        val user = userRepository.findByUsername("useruser") ?: User(
            username = "useruser",
            password = passwordEncoder.encode("useruser"),
            email = "user@test.com",
            role = Role.USER
        ).also { userRepository.save(it) }

        val adminRequest = LoginCommand(admin.username, "adminadmin")
        val userRequest = LoginCommand(user.username, "useruser")

        adminToken = loginAndGetToken(adminRequest, webTestClient)
        userToken = loginAndGetToken(userRequest, webTestClient)
    }

    private fun loginAndGetToken(request: LoginCommand, webTestClient: WebTestClient): String {
        val loginRequest = LoginCommand(username = request.username, password = request.password, rememberMe = true)
        return webTestClient.post()
            .uri("api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(loginRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(JwtAuthResponse::class.java)
            .returnResult()
            .responseBody!!.token
    }
}