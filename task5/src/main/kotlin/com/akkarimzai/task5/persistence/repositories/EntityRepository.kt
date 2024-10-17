package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.IEntityRepository
import com.akkarimzai.task5.core.domain.common.Entity
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import mu.KotlinLogging
import java.util.*

@LogExecutionTime
open class EntityRepository<T>(
    private val context: InMemoryStore<UUID, T>
) : IEntityRepository<T> where T : Entity {
    private val logger = KotlinLogging.logger {}

    override fun save(entity: T) {
        logger.info { "Saving $entity" }

        entity.id = UUID.randomUUID()
        context.collection[entity.id] = entity

        logger.info { "entity $entity saved" }
    }

    override fun load(id: UUID): T? {
        return context.collection[id]
    }

    override fun update(entity: T) {
        context.collection[entity.id] = entity
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
}