package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.application.contracts.ICategoryRepository
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.persistence.responses.KudaGoResponse
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import java.util.*

@Repository
class CategoryRepository : ICategoryRepository {
    override fun save(entity: Category) {
        logger.info { "Saving $entity" }
        entity.id = UUID.randomUUID()
        categories.add(entity).also {
            logger.info { "Category $entity created" }
        }
    }

    override fun load(id: UUID): Category? {
        logger.info { "Loading $id" }
        return categories.firstOrNull { it.id == id }.also {
            logger.info { "Loaded $it" }
        }
    }

    override fun update(entity: Category) {
        logger.info { "Updating category with id: ${entity.id}" }
        categories.removeIf { it.id == entity.id }
        categories.add(entity).also {
            logger.info { "Category $entity updated" }
        }
    }

    override fun delete(id: UUID) {
        logger.info { "Deleting category: $id" }
        categories.removeIf { it.id == id }.also {
            logger.info { "Deleted category: $it" }
        }
    }

    override fun count(): Int {
        return categories.size.also {
            logger.info { "Total categories: $it" }
        }
    }

    override fun list(pageableList: PageableList): List<Category> {
        logger.info { "Loading $pageableList categories" }
        return categories
            .drop((pageableList.page - 1) * pageableList.size)
            .take(pageableList.size).also {
                logger.info { "Loaded $pageableList categories" }
            }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        private val categories: MutableList<Category> by lazy {
            Collections.synchronizedList(mutableListOf<Category>()).apply {
                addAll(fetchCategoryList(RestTemplate()))
            }
        }

        private fun fetchCategoryList(restTemplate: RestTemplate): List<Category> {
            logger.info { "Fetching category list from kudago API" }
            val response = try {
                restTemplate.getForObject(
                    "https://kudago.com/public-api/v1.4/place-categories",
                    Array<KudaGoResponse>::class.java).also {
                    logger.info { "Loaded ${it?.size ?: 0} categories" }
                }
            } catch (e: Exception) {
                logger.debug { "Can't fetch categories from kudago API: $e" }
                null
            }
            return response?.map { Category(UUID.randomUUID(), it.slug, it.name) } ?: emptyList()
        }
    }
}