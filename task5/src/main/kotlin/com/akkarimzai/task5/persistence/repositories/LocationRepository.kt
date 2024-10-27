package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.ILocationRepository
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@LogExecutionTime
class LocationRepository(
    val context: InMemoryStore<UUID, Location>,
) : ILocationRepository, EntityRepository<Location>(context) {
    override fun slugExist(slug: String): Boolean {
        return context.collection.values.any { it.slug == slug }
    }
}