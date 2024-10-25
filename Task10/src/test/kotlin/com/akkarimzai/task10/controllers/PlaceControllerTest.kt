package com.akkarimzai.task10.controllers

import com.akkarimzai.task10.controllers.e2e.AbstractIntegrationTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PlaceControllerTest(
    @Autowired private val webTestClient: WebTestClient,
) : AbstractIntegrationTestConfig() {

}
