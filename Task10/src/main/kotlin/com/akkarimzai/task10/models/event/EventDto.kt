package com.akkarimzai.task10.models.event

import java.time.LocalDateTime

data class EventDto(
    var id: Long,
    var name: String,
    var date: LocalDateTime,
    var tagline: String?,
)