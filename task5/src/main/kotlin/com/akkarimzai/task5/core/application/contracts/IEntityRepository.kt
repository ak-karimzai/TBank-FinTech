package com.akkarimzai.task5.core.application.contracts

import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.domain.common.Entity
import java.util.UUID

interface IEntityRepository<T> where T : Entity {
    fun save(entity: T)
    fun load(id: UUID): T?
    fun update(entity: T)
    fun delete(id: UUID)
    fun count(): Int
    fun list(page: Int, size: Int): List<T>
}