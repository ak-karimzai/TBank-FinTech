package com.akkarimzai.task10.profiles

import com.akkarimzai.task10.entities.Event
import com.akkarimzai.task10.entities.Place
import com.akkarimzai.task10.models.event.CreateEventCommand
import com.akkarimzai.task10.models.event.EventDto
import com.akkarimzai.task10.models.event.UpdateEventCommand
import com.akkarimzai.task10.models.place.CreatePlaceCommand
import com.akkarimzai.task10.models.place.PlaceDto
import com.akkarimzai.task10.models.place.UpdatePlaceCommand

fun Event.toEventDto(): EventDto {
    return EventDto(
        id = this.id ?: 0,
        name = this.name,
        date = this.date,
        tagline = this.tagline
    )
}

fun Place.toPlaceDto(): PlaceDto {
    return PlaceDto(
        id = this.id ?: 0,
        name = this.name,
        address = this.address,
        description = this.description,
        subway = this.subway
    )
}

fun CreateEventCommand.toEventEntity(place: Place): Event {
    return Event(
        name = this.name,
        date = this.date,
        tagline = this.tagline,
        place = place
    )
}

fun UpdateEventCommand.toEventEntity(event: Event): Event {
    return Event(
        id = event.id,
        name = this.name ?: event.name,
        date = this.date ?: event.date,
        tagline = this.tagline ?: event.tagline,
        place = event.place
    )
}

fun CreatePlaceCommand.toPlaceEntity(): Place {
    return Place(
        name = this.name,
        address = this.address,
        description = this.description,
        subway = this.subway
    )
}

fun UpdatePlaceCommand.toPlaceEntity(place: Place): Place {
    return Place(
        id = place.id,
        name = this.name ?: place.name,
        address = this.address ?: place.address,
        description = this.description ?: place.description,
        subway = this.subway ?: place.subway
    )
}