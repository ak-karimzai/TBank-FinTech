package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class InMemoryDbContext {
    @Bean("categories")
    fun categoryInMemoryStore(): InMemoryStore<UUID, Category> {
        return InMemoryStore()
    }

    @Bean("locations")
    fun locationInMemoryStore(): InMemoryStore<UUID, Location> {
        return InMemoryStore()
    }
}