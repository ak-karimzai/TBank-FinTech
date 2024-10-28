package com.akkarimzai.task5.persistence.features.commands

import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.persistence.repositories.observers.IEntityObserver
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class LocationInitializingCommand(
    private val newsService: INewsClient,
    private val observers: List<IEntityObserver<Location>>
) : ICommand {
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        initializeLocations()
    }

    private fun initializeLocations() {
        newsService.fetchLocations().let {
            it.forEach { kudagoLocation ->
                val location = Location(
                    slug = kudagoLocation.slug,
                    name = kudagoLocation.name
                )
                observers.forEach { it.update(location) }
            }
            logger.info { "${it.size} locations fetched successfully" }
        }
    }
}