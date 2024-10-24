package com.akkarimzai.task10.services

import com.akkarimzai.task10.models.place.*
import com.akkarimzai.task10.profiles.toPlaceEntity
import com.akkarimzai.task10.repositories.PlaceRepository
import com.akkarimzai.task10.repositories.PlaceRepositoryMock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.domain.PageRequest
import java.util.Optional

class PlaceServiceTest(
    private val repository: PlaceRepository = mockk<PlaceRepository>(),
    private val service: PlaceService = PlaceService(repository),
) : FunSpec({
    beforeTest { clearMocks(repository) }

    test("create should creates place") {
        // Arrange
        val command = CreatePlaceCommand(name = "Test name", address = "Test address", description = "Test description", subway = "Test subway")
        val place = command.toPlaceEntity()
        every { repository.save(eq(place)) } returns place

        // Act
        service.create(command)

        // Assert
        verify(exactly = 1) { repository.save(place) }
    }

    test("update should updates place") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val command = UpdatePlaceCommand(name = "Test name", address = "Test address", description = "Test description", subway = null)
        val placetoUpdate = command.toPlaceEntity(place)
        every { repository.findById(place.id!!) } returns Optional.of(place)
        every { repository.save(placetoUpdate) } returns placetoUpdate

        // Act
        service.update(place.id!!, command)

        // Assert
        verify(exactly = 1) { repository.save(placetoUpdate) }
    }

    test("delete should delete place by id") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val command = DeletePlaceCommand(place.id!!)
        every { repository.findById(place.id!!) } returns Optional.of(place)
        every { repository.delete(place) } just Runs

        // Act
        service.delete(command)

        // Assert
        verify(exactly = 1) { repository.findById(place.id!!) }
        verify(exactly = 1) { repository.delete(place) }
    }

    test("get should return place by id") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val query = GetPlaceQuery(place.id!!)
        every { repository.findById(place.id!!) } returns Optional.of(place)

        // Act
        val placeDto = service.get(query)

        // Assert
        verify(exactly = 1) { repository.findById(place.id!!) }
        placeDto.name shouldBeEqual place.name
        placeDto.address shouldBeEqual place.address
        placeDto.description shouldBeEqual place.description
        placeDto.subway?.shouldBeEqual(place.subway!!)
    }

    test("list should return paged place list") {
        // Arrange
        val pagedPlaces = PlaceRepositoryMock.getPageList()
        val query = ListPlaceQuery(1, 10)
        every { repository.findAll(PageRequest.of(query.page, query.size)) } returns pagedPlaces

        // Act
        val result = service.list(query)

        // Assert
        verify(exactly = 1) { repository.findAll(PageRequest.of(query.page, query.size)) }
        result.size shouldBe pagedPlaces.size
    }
})
