package com.akkarimzai.task5.persistence

import com.akkarimzai.task5.core.domain.common.Entity
import com.akkarimzai.task5.persistence.features.commands.CategoryInitializingCommand
import com.akkarimzai.task5.persistence.features.commands.LocationInitializingCommand
import com.akkarimzai.task5.persistence.repositories.observers.IEntityObserver
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@Service
class DataInitializer(
    @Autowired @Qualifier("fixedThreadPool") private val fixedThreadPool: ExecutorService,
    @Autowired @Qualifier("scheduledThreadPool") private val scheduledThreadPool: ExecutorService,
    @Autowired private val categoryInitializer: CategoryInitializingCommand,
    @Autowired private val locationInitializer: LocationInitializingCommand,
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
            val categoriesFuture = fixedThreadPool.submit { categoryInitializer.execute() }
            val locationsFuture = fixedThreadPool.submit { locationInitializer.execute()}

            categoriesFuture.get()
            locationsFuture.get()
        }
        logger.info { "Data initialization completed in $timeTaken ms" }
    }

    fun <T : Entity> notifyObservers(observers: List<IEntityObserver<T>>, entity: T) {
        observers.forEach { it.update(entity) }
    }
}