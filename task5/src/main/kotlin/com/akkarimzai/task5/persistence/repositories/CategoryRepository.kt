package com.akkarimzai.task5.persistence.repositories

import com.akkarimzai.task5.core.application.contracts.ICategoryRepository
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.persistence.responses.KudaGoResponse
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import java.util.*

@Repository
class CategoryRepository(
    context: InMemoryStore<UUID, Category>
) : ICategoryRepository, EntityRepository<Category>(context)