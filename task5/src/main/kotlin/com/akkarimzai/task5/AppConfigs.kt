package com.akkarimzai.task5


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AppConfigs {
    @Bean
    fun taskExecutor(@Value("\${concurrency.completable-future.core-pool-size}") corePoolSize: Int,
                     @Value("\${concurrency.completable-future.max-pool-size}") maxPoolSize: Int,
                     @Value("\${concurrency.completable-future.queue-capacity}") queueCapacity: Int) : Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = corePoolSize
        executor.maxPoolSize = maxPoolSize
        executor.queueCapacity = queueCapacity
        executor.setThreadNamePrefix("CompletableFuture-Thread-")
        executor.initialize()
        return executor
    }
}