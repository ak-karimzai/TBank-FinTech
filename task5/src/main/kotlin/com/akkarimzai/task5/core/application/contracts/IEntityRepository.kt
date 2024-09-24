package com.akkarimzai.task5.core.application.contracts

import com.akkarimzai.task5.core.application.models.PageableList
import java.util.UUID

interface IEntityRepository<T> {
    fun save(entity: T)
    fun load(id: UUID): T?
    fun update(entity: T)
    fun delete(id: UUID)
    fun count(): Int
    fun list(pageableList: PageableList): List<T>
}