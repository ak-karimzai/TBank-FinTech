package com.akkarimzai.task10.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_seq")
    @SequenceGenerator(name = "events_seq", sequenceName = "events_seq", allocationSize = 1)
    var id: Long? = null,
    var name: String,
    var date: LocalDateTime,
    var tagline: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    var place: Place
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false
        if (name != other.name) return false
        if (date != other.date) return false
        if (tagline != other.tagline) return false
        if (place != other.place) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + (tagline?.hashCode() ?: 0)
        result = 31 * result + place.hashCode()
        return result
    }
}