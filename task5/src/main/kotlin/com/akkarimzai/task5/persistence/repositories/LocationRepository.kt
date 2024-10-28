package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.ISnapshotable
import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ILocationRepository
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.core.domain.entities.Snapshot
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@LogExecutionTime
class LocationRepository(
    val context: InMemoryStore<UUID, Location>,
    history: InMemoryStore<UUID, MutableList<Snapshot<Location>>>,
) : ILocationRepository,
    ISnapshotable<Location>,
    EntityRepository<Location>(context, history) {
    override fun slugExist(slug: String): Boolean {
        return context.collection.values.any { it.slug == slug }
    }
}