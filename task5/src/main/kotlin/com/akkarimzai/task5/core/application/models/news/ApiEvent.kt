package com.akkarimzai.task5.core.application.models.news

data class ApiEvent(
    val id: Int,
    val dates: List<Date>,
    val title: String,
    val price: String
) {
    data class Date(
        val start: Long,
        val end: Long,
    )
}