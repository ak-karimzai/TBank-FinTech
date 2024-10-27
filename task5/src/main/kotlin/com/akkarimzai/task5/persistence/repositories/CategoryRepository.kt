package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.persistence.ICategoryRepository
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CategoryRepository(
    context: InMemoryStore<UUID, Category>,
) : ICategoryRepository, EntityRepository<Category>(context)