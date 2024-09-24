package com.akkarimzai.task5.persistence.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class PersistenceConfigs {
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}