package com.akkarimzai.task10.controllers.e2e

import com.akkarimzai.task10.models.user.JwtAuthResponse
import com.akkarimzai.task10.models.user.LoginCommand
import com.akkarimzai.task10.models.user.RegisterCommand
import io.kotest.matchers.equals.shouldNotBeEqual
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest(
    @Autowired private val webTestClient: WebTestClient
) : AbstractIntegrationTestConfig() {
    @Test
    fun `should register and login user successfully`() {
        val registerRequest = RegisterCommand("username", "test@test.com", "password")
        val loginRequest = LoginCommand("username", "password")

        val responseBody = webTestClient.post()
            .uri("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(registerRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(JwtAuthResponse::class.java)
            .returnResult()
            .responseBody!!
        responseBody.token.length shouldNotBeEqual 0

        webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(JwtAuthResponse::class.java)
            .returnResult()
            .responseBody!!
        responseBody.token.length shouldNotBeEqual 0
    }

    @Test
    fun `should return bad request after logout`() {
        val registerRequest = RegisterCommand("username1", "test1@test.com", "password1")
        val loginRequest = LoginCommand("username1", "password1")

        val responseBody = webTestClient.post()
            .uri("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(registerRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(JwtAuthResponse::class.java)
            .returnResult()
            .responseBody!!
        responseBody.token.length shouldNotBeEqual 0

        val loginResponseBody = webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(JwtAuthResponse::class.java)
            .returnResult()
            .responseBody!!
        loginResponseBody.token.length shouldNotBeEqual 0

        webTestClient.post()
            .uri("/api/auth/logout")
            .header("Authorization", "Bearer ${loginResponseBody.token}")
            .exchange()
            .expectStatus().isNoContent
    }
}