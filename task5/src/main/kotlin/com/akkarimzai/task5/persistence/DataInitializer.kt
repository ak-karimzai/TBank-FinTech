package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.domain.common.Entity
import com.akkarimzai.task5.persistence.repositories.CategoryRepository
import com.akkarimzai.task5.persistence.repositories.LocationRepository
import com.akkarimzai.task5.persistence.repositories.observers.EntityObserver
import com.akkarimzai.task5.persistence.repositories.observers.IEntityObserver
import com.akkarimzai.task5.persistence.utils.InMemoryStore
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@Service
class DataInitializer(
    @Autowired @Qualifier("fixedThreadPool") private val fixedThreadPool: ExecutorService,
    @Autowired @Qualifier("scheduledThreadPool") private val scheduledThreadPool: ExecutorService,
    @Autowired private val categoryObservers: List<IEntityObserver<Category>>,
    @Autowired private val locationObservers: List<IEntityObserver<Location>>,
    @Autowired private val newsService: INewsClient,
    @Value("\${concurrency.initialize.delay}") private val scheduleDuration: Duration
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        scheduledThreadPool.submit {
            scheduleInitialization()
        }
    }

    private fun scheduleInitialization() {
        val task = Runnable { initializeData() }
        val scheduledExecutor = scheduledThreadPool as ScheduledExecutorService
        scheduledExecutor.scheduleWithFixedDelay(task, 0, scheduleDuration.toMillis(), TimeUnit.MILLISECONDS)
    }

    private fun initializeData() {
        val timeTaken = measureTimeMillis {
            val categoriesFuture = fixedThreadPool.submit { initializeCategories() }
            val locationsFuture = fixedThreadPool.submit { initializeLocations() }

            categoriesFuture.get()
            locationsFuture.get()
        }
        logger.info { "Data initialization completed in $timeTaken ms" }
    }

    fun initializeCategories() {
        newsService.fetchCategories().let {
            it.forEach { kudagoCategory ->
                val category = Category(
                    slug = kudagoCategory.slug,
                    name = kudagoCategory.name
                )
                notifyObservers(categoryObservers, category)
            }
            logger.info { "${it.size} categories fetched successfully" }
        }
    }

    fun initializeLocations() {
        newsService.fetchLocations().let {
            it.forEach { kudagoLocation ->
                val location = Location(
                    slug = kudagoLocation.slug,
                    name = kudagoLocation.name
                )
                notifyObservers(locationObservers, location)
            }
            logger.info { "${it.size} locations fetched successfully" }
        }
    }

    fun <T : Entity> notifyObservers(observers: List<IEntityObserver<T>>, entity: T) {
        observers.forEach { it.update(entity) }
    }
}