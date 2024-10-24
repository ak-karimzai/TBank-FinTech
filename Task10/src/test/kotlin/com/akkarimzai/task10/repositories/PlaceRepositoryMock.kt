package com.akkarimzai.task10.repositories

import com.akkarimzai.task10.entities.Place
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

object PlaceRepositoryMock {
    fun getSinglePlace(): Place {
        return Place(
            id = 1,
            name = "Test place",
            address = "Test address",
            description = "Test description",
            subway = "Test subway"
        )
    }

    fun getPlaceList(): List<Place> {
        return listOf(
            Place(1, "Test place1", "Test address1", "Test description1", "Test subway1"),
            Place(2, "Test place2", "Test address2", "Test description2", "Test subway2"),
            Place(3, "Test place3", "Test address3", "Test description3", "Test subway3"),
        )
    }

    fun getPageList(): Page<Place> {
        return PageImpl(getPlaceList())
    }
}