package com.akkarimzai.task5.core.application.profiles

import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.application.models.news.ApiEvent
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Event
import com.akkarimzai.task5.core.domain.entities.Location
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.microseconds

fun CreateCategory.toEntity(): Category {
    return Category(
        name = this.name,
        slug = this.slug
    )
}

fun CreateLocation.toEntity(): Location {
    return Location(
        name = this.name,
        slug = this.slug
    )
}

fun ApiEvent.toEntity(): Event {
    val regex = Regex("[-+]?[0-9]*\\.?[0-9]+")
    val matchResult = regex.find(this.price)
    val firstDouble = matchResult?.value?.toDoubleOrNull()
    val firstDate = this.dates[0]
    val date = Event.Date(
        start = Instant.ofEpochSecond(firstDate.start)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        end = Instant.ofEpochSecond(firstDate.end)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
    )
    return Event(
        dates = date,
        title = this.title,
        price = firstDouble ?: 0.0
    )
}