package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location
import com.akkarimzai.task5.infrastructure.services.INewsService
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
    @Autowired @Qualifier("categories") private val categories: InMemoryStore<UUID, Category>,
    @Autowired @Qualifier("locations") private val locations: InMemoryStore<UUID, Location>,
    @Autowired private val newsService: INewsService,
    @Value("\${api.kudago.categories}") private val categoriesUri: String,
    @Value("\${api.kudago.locations}") private val locationsUri: String,
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
        newsService.fetchCategories(categoriesUri).let { it ->
            it.forEach { kudagoCategory ->
                val id = UUID.randomUUID()
                categories.collection[id] = Category(
                    id = id,
                    slug = kudagoCategory.slug,
                    name = kudagoCategory.name
                )
            }
            logger.info { "${it.size} categories fetched successfully" }
        }
    }

    fun initializeLocations() {
        newsService.fetchLocations(locationsUri).let { it ->
            it.forEach { kudagoLocation ->
                val id = UUID.randomUUID()
                locations.collection[id] = Location(
                    id = id,
                    slug = kudagoLocation.slug,
                    name = kudagoLocation.name
                )
            }
            logger.info { "${it.size} locations fetched successfully" }
        }
    }
}