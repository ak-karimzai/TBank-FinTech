package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.persistence.IEntityRepository
import com.akkarimzai.task5.core.application.exceptions.NotFoundException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.category.UpdateCategory
import com.akkarimzai.task5.core.application.profiles.toEntity
import com.akkarimzai.task5.core.domain.entities.Category
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val repository: IEntityRepository<Category>) {
    private val logger = KotlinLogging.logger {}

    fun save(newCategory: CreateCategory) : UUID {
        logger.info { "Saving new category $newCategory" }
        val validationResult: List<String> = newCategory.validate()
        if (validationResult.isNotEmpty())
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }

        val entity = newCategory.toEntity()

        logger.info { "Saving new category $entity" }
        repository.save(entity)
        return entity.id.also {
            logger.info { "Successfully saved new category $entity" }
        }
    }

    fun load(id: UUID) : Category {
        logger.info { "Loading category $id" }
        return repository.load(id).also {
            logger.info { "Successfully loaded category $it" }
        } ?: throw NotFoundException("Location", id).also {
            logger.error { "Location: $id not found" }
        }
    }

    fun list(pageableList: PageableList) : PaginatedList<Category> {
        logger.info { "Loading page of categories $pageableList" }
        val validationResult: List<String> = pageableList.validate()
        if (validationResult.isNotEmpty())
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }

        val list = repository.list(pageableList.page, pageableList.size)
        val count = repository.count()

        return PaginatedList(
            list,
            count,
            pageableList.page,
            pageableList.size
        ).also {
            logger.info { "Successfully loaded category list with page: ${it.page}, total available pages: ${it.totalPages}" }
        }
    }

    fun update(id: UUID, updateCategory: UpdateCategory) {
        logger.info { "Updating category $id, category info $updateCategory" }
        val validationResult: List<String> = updateCategory.validate()
        if (validationResult.isNotEmpty())
            throw ValidationException(validationResult).also {
                logger.error { "Validation failure, validation result: ${it.validationResult}" }
            }

        val categoryToUpdate = this.load(id)

        updateCategory.slug?.let {
            categoryToUpdate.slug = it
        }

        updateCategory.name?.let {
            categoryToUpdate.name = it
        }

        repository.update(categoryToUpdate).also {
            logger.info { "Successfully updated category $id, category info $updateCategory" }
        }
    }

    fun delete(id: UUID) {
        logger.info { "Deleting category $id" }
        val categoryToDelete = this.load(id)

        repository.delete(id).also {
            logger.info { "Successfully deleted category $id, category info $categoryToDelete" }
        }
    }
}