package com.akkarimzai.task10.repositories

import com.akkarimzai.task10.entities.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

object EventRepositoryMock {
    fun getSingleEvent(): Event {
        return Event(
            id = 10,
            name = "Test Event",
            date = LocalDateTime.of(2020, 1, 1, 1, 1),
            tagline = "Test tagline",
            place = PlaceRepositoryMock.getSinglePlace()
        )
    }

    fun getEventList(): List<Event> {
        return listOf(
            Event(id = 1, name = "Test Event1", date = LocalDateTime.of(2020, 1, 1, 1, 1), tagline = "Test tagline1", place = PlaceRepositoryMock.getSinglePlace()),
            Event(id = 2, name = "Test Event2", date = LocalDateTime.of(2020, 1, 1, 1, 1), tagline = "Test tagline2", place = PlaceRepositoryMock.getSinglePlace()),
            Event(id = 3, name = "Test Event3", date = LocalDateTime.of(2020, 1, 1, 1, 1), tagline = "Test tagline3", place = PlaceRepositoryMock.getSinglePlace()),
            Event(id = 4, name = "Test Event4", date = LocalDateTime.of(2020, 1, 1, 1, 1), tagline = "Test tagline4", place = PlaceRepositoryMock.getSinglePlace()),
        )
    }

    fun getEventPage(): Page<Event> {
        return PageImpl(getEventList())
    }
}