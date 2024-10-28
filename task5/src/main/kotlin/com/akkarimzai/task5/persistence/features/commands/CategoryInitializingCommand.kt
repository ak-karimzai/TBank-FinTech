package com.akkarimzai.task5.persistence.features.commands

import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.persistence.repositories.observers.IEntityObserver
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CategoryInitializingCommand(
    private val newsService: INewsClient,
    private val observers: List<IEntityObserver<Category>>
) : ICommand {
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        initializeCategories()
    }

    private fun initializeCategories() {
        newsService.fetchCategories().let {
            it.forEach { kudagoCategory ->
                val category = Category(
                    slug = kudagoCategory.slug,
                    name = kudagoCategory.name
                )
                observers.forEach { it.update(category) }
            }
            logger.info { "${it.size} categories fetched successfully" }
        }
    }
}