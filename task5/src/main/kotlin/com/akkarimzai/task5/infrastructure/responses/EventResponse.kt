package com.akkarimzai.task5.infrastructure.responses

import com.akkarimzai.task5.core.application.models.news.ApiEvent
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventResponse(
    val results: List<ApiEvent>
)