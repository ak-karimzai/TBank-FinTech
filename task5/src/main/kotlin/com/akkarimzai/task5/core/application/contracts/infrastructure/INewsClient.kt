package com.akkarimzai.task5.core.application.contracts.infrastructure

import com.akkarimzai.task5.core.application.models.news.ApiCategory
import com.akkarimzai.task5.core.application.models.news.ApiEvent
import com.akkarimzai.task5.core.application.models.news.ApiLocation
import java.time.LocalDate

interface INewsClient {
    fun fetchCategories(): List<ApiCategory>
    fun fetchLocations(): List<ApiLocation>
    fun fetchEvents(dateFrom: LocalDate, dateTo: LocalDate): List<ApiEvent>
}