package com.akkarimzai.task10.repositories

import com.akkarimzai.task10.entities.Place
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlaceRepository : JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.events WHERE p.id = :placeId")
    fun findByIdWithEvents(placeId: Long): List<Place>
}
