package com.akkarimzai.task10.services

import com.akkarimzai.task10.entities.Event
import com.akkarimzai.task10.entities.Place
import com.akkarimzai.task10.exceptions.ValidationException
import com.akkarimzai.task10.models.event.*
import com.akkarimzai.task10.profiles.toEventDto
import com.akkarimzai.task10.profiles.toEventEntity
import com.akkarimzai.task10.repositories.EventRepository
import com.akkarimzai.task10.repositories.EventRepositoryMock
import com.akkarimzai.task10.repositories.PlaceRepository
import com.akkarimzai.task10.repositories.PlaceRepositoryMock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class EventServiceTest(
    private val eventRepository: EventRepository = mockk<EventRepository>(),
    private val placeRepository: PlaceRepository = mockk<PlaceRepository>(),
    private val service: EventService = EventService(eventRepository, placeRepository)
) : FunSpec({
    beforeTest {
        clearMocks(eventRepository)
        clearMocks(placeRepository)
    }

    test("create should create event") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val event = EventRepositoryMock.getSingleEvent()
        val command = CreateEventCommand(
            name = "Test Event", date = LocalDateTime.of(2020, 1, 1, 1, 1, 1), tagline = "test tagline")

        every { placeRepository.findById(place.id!!) } returns Optional.of(place)
        every { eventRepository.save(any()) } returns event

        // Act
        val createdEventId = service.create(place.id!!, command)

        // Assert
        createdEventId shouldNotBe null
        createdEventId?.shouldBeEqual(event.id!!)
    }

    test("create command with invalid name should throw validation exception") {
        // Arrange
        val command = CreateEventCommand(
            name = "", date = LocalDateTime.of(2020, 1, 1, 1, 1, 1), tagline = "test tagline")

        // Act && Assert
        assertThrows<ValidationException> {
            service.create(10, command)
        }
    }

    test("update should update event") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val command = UpdateEventCommand(
            name = "Test Event", date = LocalDateTime.of(2020, 1, 1, 1, 1, 1), "test tagline")
        val event = EventRepositoryMock.getSingleEvent()
        val updatedEvent = command.toEventEntity(event)

        every { eventRepository.findById(event.id!!) } returns Optional.of(event)
        every { eventRepository.save(eq(updatedEvent)) } returns updatedEvent

        // Act
        service.update(place.id!!, event.id!!, command)

        // Assert
        verify(exactly = 1) { eventRepository.findById(event.id!!) }
        verify(exactly = 1) { eventRepository.save(any()) }
    }

    test("update with invalid name command should throw validation exception") {
        // Arrange
        val place = PlaceRepositoryMock.getSinglePlace()
        val event = EventRepositoryMock.getSingleEvent()
        val command = UpdateEventCommand(
            name = "", date = LocalDateTime.of(2020, 1, 1, 1, 1, 1), "test tagline")


        // Act && Assert
        assertThrows<ValidationException> {
            service.update(place.id!!, event.id!!, command)
        }
    }

    test("delete should remove event") {
        // Arrange
        val placeId = 10L
        val eventId = 10L
        val place = Place(placeId, "Test title", "Test description", "Test description")
        val event = Event(id = placeId, name = "Test name", date = LocalDateTime.now(), tagline = "Test tagline", place = place)
        val command = DeleteEventCommand(placeId, eventId)
        every { placeRepository.findById(placeId) } returns Optional.of(place)
        every { eventRepository.findById(eventId) } returns Optional.of(event)
        every { eventRepository.delete(event) } just Runs

        // Act
        service.delete(command)

        // Assert
        verify(exactly = 1) { eventRepository.delete(event) }
        verify(exactly = 1) { eventRepository.findById(eventId) }
    }

    test("delete with invalid id should return validation exception") {
        // Arrange
        val command = DeleteEventCommand(-10L, 10L)

        // Act && Assert
        assertThrows<ValidationException> {
            service.delete(command)
        }
    }

    test("get should return event by id") {
        // Arrange
        val placeId = 10L
        val eventId = 10L
        val place = Place(placeId, "Test title", "Test description", "Test description")
        val event = Event(id = placeId, name = "Test name", date = LocalDateTime.now(), tagline = "Test tagline", place = place)
        every { eventRepository.findById(eventId) } returns Optional.of(event)

        // Act
        val loadedEvent = service.get(GetEventQuery(eventId, placeId))

        // Arrange
        verify { eventRepository.findById(eventId) }
        loadedEvent shouldBeEqual event.toEventDto()
    }

    test("get with invalid id should throw validation exception") {
        // Arrange
        val query = GetEventQuery(-10L, 10L)

        // Act && Assert
        assertThrows<ValidationException> {
            service.get(query)
        }
    }

    test("list should return event paged list") {
        // Arrange
        val placeId = 10L
        val page = 1
        val size = 10
        every { placeRepository.existsById(placeId) } returns true
        every { eventRepository.findAll(any(), PageRequest.of(page, size)) } returns EventRepositoryMock.getEventPage()

        // Act
        val pagedEventList = service.list(placeId, query = ListEventsQuery(page = page, size = size))

        // Assert
        verify(exactly = 1) { eventRepository.findAll(any(), PageRequest.of(page, size)) }
        pagedEventList.size shouldNotBeEqual 0
    }

    test("list with invalid page should throw validation exception") {
        // Arrange
        val placeId = 10L
        val page = 0
        val size = 10

        // Act && Assert
        assertThrows<ValidationException> {
            service.list(placeId, query = ListEventsQuery(page = page, size = size))
        }
    }
})
