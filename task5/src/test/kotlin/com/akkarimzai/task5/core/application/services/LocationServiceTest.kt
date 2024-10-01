package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.ILocationRepository
import com.akkarimzai.task5.core.application.exceptions.NotFoundException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.application.models.location.UpdateLocation
import com.akkarimzai.task5.core.application.profiles.toEntity
import com.akkarimzai.task5.core.domain.entities.Location
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.assertThrows
import java.util.*

class LocationServiceTest : FunSpec() {
    init {
        context("location service tests") {
            test("save executes normally") {
                // Arrange
                val createLocation = CreateLocation("test", "test")
                val entity = createLocation.toEntity()
                every { repository.save(entity) } just Runs
                every { repository.slugExist(createLocation.slug) } returns false

                // Act
                service.save(createLocation)

                // Assert
                verify(exactly = 1) { repository.save(entity) }
                verify(exactly = 1) { repository.slugExist(createLocation.slug) }
            }

            test("save with invalid request should throw ValidationException") {
                // Arrange
                val createLocation = CreateLocation("", "test")

                // Act && Assert
                assertThrows<ValidationException> { service.save(createLocation) }
            }

            test("load executes normally") {
                // Arrange
                val id = UUID.randomUUID()
                val location = Location(id, "test", "test")
                every { repository.load(id) } returns location

                // Act
                val loadedLocation = service.load(id)

                // Assert
                verify(exactly = 1) { repository.load(id) }
                location shouldBeEqual loadedLocation
            }

            test("load when location not exist throws NotFoundException") {
                // Arrange
                val id = UUID.randomUUID()
                every { repository.load(id) } returns null

                // Act && Assert
                assertThrows<NotFoundException> {
                    service.load(id)
                }
            }

            test("list executes normally") {
                // Arrange
                val pageableList = PageableList(1, 10)
                every { repository.list(pageableList.page, pageableList.size) } returns listOf()
                every { repository.count() } returns 0

                // Act
                val paginatedList = service.list(pageableList)

                // Assert
                paginatedList.items shouldBe emptyList<Location>()
            }

            test("list with incorrect page should throw ValidationException") {
                // Arrange
                val pageableList = PageableList(0, 10)

                // Act && Assert
                assertThrows<ValidationException> {
                    service.list(pageableList)
                }
            }

            test("update executes normally") {
                // Arrange
                val id = UUID.randomUUID()
                val updateLocation = UpdateLocation("test", "test")
                val entity = Location(id, "test", "test")
                every { repository.slugExist(entity.slug) } returns false
                every { repository.load(id) } returns entity
                every { repository.update(entity) } just Runs

                // Act
                service.update(id, updateLocation)

                // Assert
                verify(exactly = 1) { repository.update(entity) }
            }

            test("update should throw ValidationException when request is invalid") {
                // Arrange
                val id = UUID.randomUUID()
                val updateLocation = UpdateLocation("", "test")

                // Act && Assert
                assertThrows<ValidationException> {
                    service.update(id, updateLocation)
                }
            }

            test("delete executes normally") {
                // Arrange
                val id = UUID.randomUUID()
                val entity = Location(id, "test", "test")
                every { repository.load(id) } returns entity
                every { repository.delete(id) } just Runs

                // Act
                service.delete(id)

                // Assert
                verify(exactly = 1) { repository.load(id) }
                verify(exactly = 1) { repository.delete(id) }
            }
        }
    }
    var repository: ILocationRepository = mockk()
    val service: LocationService = LocationService(repository)
}