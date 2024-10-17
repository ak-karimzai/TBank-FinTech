package com.akkarimzai.task5.core.domain.entities

import java.time.LocalDate

class Event(
    var dates: Date,
    var title: String,
    var price: Double
) {
    data class Date(
        val start: LocalDate,
        val end: LocalDate,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (dates != other.dates) return false
        if (title != other.title) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dates.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(dates=$dates, title='$title', price=$price)"
    }
}