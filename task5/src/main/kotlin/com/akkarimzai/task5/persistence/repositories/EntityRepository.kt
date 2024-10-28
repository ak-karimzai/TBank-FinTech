package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.ISnapshotable
import com.akkarimzai.task5.core.application.contracts.persistence.repositories.IEntityRepository
import com.akkarimzai.task5.core.domain.common.Entity
import com.akkarimzai.task5.core.domain.entities.Snapshot
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import mu.KotlinLogging
import java.util.*

@LogExecutionTime
open class EntityRepository<T : Entity>(
    private val context: InMemoryStore<UUID, T>,
    private val history: InMemoryStore<UUID, MutableList<Snapshot<T>>>
) : IEntityRepository<T>, ISnapshotable<T> {
    private val logger = KotlinLogging.logger {}

    override fun save(entity: T): T {
        logger.info { "Saving $entity" }

        entity.id = UUID.randomUUID()
        context.collection[entity.id!!] = entity

        return entity.also {
            logger.info { "entity $entity saved" }
        }
    }

    override fun load(id: UUID): T? {
        return context.collection[id]
    }

    override fun update(entity: T) {
        createSnapshot(entity)
        context.collection[entity.id!!] = entity
    }

    override fun delete(id: UUID) {
        context.collection.remove(id)
    }

    override fun count(): Int {
        return context.collection.values.size
    }

    override fun list(page: Int, size: Int): List<T> {
        return context.collection.values
            .drop((page - 1) * size)
            .take(size)
    }

    override fun createSnapshot(state: T) {
        if (history.collection[state.id!!] == null) {
            history.collection[state.id!!] = mutableListOf()
        }
        history.collection[state.id!!]!!.add(Snapshot(state = state))
    }

    override fun getSnapshots(id: UUID): List<Snapshot<T>>? {
        return history.collection[id]
    }

    override fun getLastState(id: UUID): Snapshot<T>? {
        return history.collection[id]?.last()
    }
}