package com.akkarimzai.task5.persistence.repositories.observers

import com.akkarimzai.task5.core.domain.common.Entity

interface IEntityObserver<T> where T : Entity {
    fun update(entity: T)
}