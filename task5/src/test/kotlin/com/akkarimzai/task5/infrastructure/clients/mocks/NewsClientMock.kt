package com.akkarimzai.task5.infrastructure.clients.mocks

import com.akkarimzai.task5.core.application.contracts.infrastructure.INewsClient
import com.akkarimzai.task5.core.application.models.news.ApiCategory
import com.akkarimzai.task5.core.application.models.news.ApiEvent
import com.akkarimzai.task5.core.application.models.news.ApiLocation
import java.time.LocalDate
import java.time.ZoneId

class NewsClientMock : INewsClient {
    override fun fetchCategories(): List<ApiCategory> {
        TODO("Not yet implemented")
    }

    override fun fetchLocations(): List<ApiLocation> {
        TODO("Not yet implemented")
    }

    override fun fetchEvents(dateFrom: LocalDate, dateTo: LocalDate): List<ApiEvent> {
        return listOf(
            ApiEvent(1, listOf(ApiEvent.Date(localDateToEpochSeconds(dateFrom), localDateToEpochSeconds(dateTo))), "Test", "5000.0"),
            ApiEvent(2, listOf(ApiEvent.Date(localDateToEpochSeconds(dateFrom), localDateToEpochSeconds(dateTo))), "Test1", "5000.0"),
            ApiEvent(3, listOf(ApiEvent.Date(localDateToEpochSeconds(dateFrom), localDateToEpochSeconds(dateTo))), "Test2", "5000.0"),
            ApiEvent(4, listOf(ApiEvent.Date(localDateToEpochSeconds(dateFrom), localDateToEpochSeconds(dateTo))), "Test3", "5000.0"),
        )
    }

    private fun localDateToEpochSeconds(localDate: LocalDate): Long {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
    }
}