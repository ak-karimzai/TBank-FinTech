package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.ISnapshotable
import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ICategoryRepository
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Snapshot
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CategoryRepository(
    context: InMemoryStore<UUID, Category>,
    history: InMemoryStore<UUID, MutableList<Snapshot<Category>>>,
) : ICategoryRepository,
    ISnapshotable<Category>,
    EntityRepository<Category>(context, history)