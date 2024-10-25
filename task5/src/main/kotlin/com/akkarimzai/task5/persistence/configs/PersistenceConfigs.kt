package com.akkarimzai.task5.persistence.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class PersistenceConfigs {
    @Bean("news-client")
    fun newsClient(@Value("\${api.kudago.base-url}") baseUrl: String): RestClient {
        return RestClient.create(baseUrl)
    }

    @Bean("currency-client")
    fun currencyClient(@Value("\${api.currency.base-url}") baseUrl: String): RestClient {
        return RestClient.create(baseUrl)
    }

    @Bean("fixedThreadPool")
    fun fixedThreadPool(@Value("\${concurrency.thread-pool.executors}") executors: Int): ExecutorService {
        return Executors.newFixedThreadPool(executors) { runnable ->
            Thread(runnable).apply {
                name = "fixed-thread-pool-thread-$id"
            }
        }
    }

    @Bean("scheduledThreadPool")
    fun scheduledThreadPool(@Value("\${concurrency.thread-pool.schedulers}") schedulers: Int): ExecutorService {
        return Executors.newScheduledThreadPool(schedulers) { runnable ->
            Thread(runnable).apply {
                name = "scheduled-thread-pool-thread-$id"
            }
        }
    }
}