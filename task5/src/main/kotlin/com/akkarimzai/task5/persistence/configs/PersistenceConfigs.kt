package com.akkarimzai.task5.persistence.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class PersistenceConfigs {
    @Value("\${api.kudago.base-url}")
    private lateinit var baseUrl: String

    @Bean
    fun restClient(): RestClient {
        return RestClient.create(baseUrl)
    }
}