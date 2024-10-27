package com.akkarimzai.task5.persistence.repositories.observers

import com.akkarimzai.task5.core.application.contracts.persistence.IEntityRepository
import com.akkarimzai.task5.core.domain.common.Entity

open class EntityObserver<T : Entity>(
    private val repository: IEntityRepository<T>
) : IEntityObserver<T> {
    override fun update(entity: T) {
        repository.save(entity)
    }
}