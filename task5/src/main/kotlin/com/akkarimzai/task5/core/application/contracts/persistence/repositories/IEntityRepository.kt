package com.akkarimzai.task5.core.application.contracts.persistence.repositories

import com.akkarimzai.task5.core.domain.common.Entity
import java.util.UUID

interface IEntityRepository<T> where T : Entity {
    fun save(entity: T): T
    fun load(id: UUID): T?
    fun update(entity: T)
    fun delete(id: UUID)
    fun count(): Int
    fun list(page: Int, size: Int): List<T>
}