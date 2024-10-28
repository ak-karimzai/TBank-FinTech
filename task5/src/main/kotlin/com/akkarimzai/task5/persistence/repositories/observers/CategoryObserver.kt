package com.akkarimzai.task5.persistence.repositories.observers

import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ICategoryRepository
import com.akkarimzai.task5.core.domain.entities.Category
import org.springframework.stereotype.Service

@Service
class CategoryObserver(
    repository: ICategoryRepository
) : EntityObserver<Category>(repository)